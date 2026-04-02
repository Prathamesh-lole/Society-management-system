package com.example.societymainteinance.service;

import com.example.societymainteinance.entity.ComplaintEntity;
import com.example.societymainteinance.entity.MaintenanceBillEntity;
import com.example.societymainteinance.entity.ResidentEntity;
import com.example.societymainteinance.entity.VendorEntity;
import com.example.societymainteinance.enums.VendorStatus;
import com.example.societymainteinance.repo.ComplaintRepo;
import com.example.societymainteinance.repo.MaintenanceBillRepo;
import com.example.societymainteinance.repo.ResidentRepo;
import com.example.societymainteinance.repo.VendorRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepo complaintRepo;

    @Autowired
    private ResidentRepo residentRepo;

    @Autowired
    private VendorRepo vendorRepo;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private MaintenanceBillRepo maintenanceBillRepo;

    // ===================== CREATE =====================

    // Resident raises a complaint
    public ComplaintEntity createComplaint(Long residentId, ComplaintEntity complaint) {

        ResidentEntity resident = residentRepo.findById(residentId)
                .orElseThrow(() -> new RuntimeException("Resident not found"));

        complaint.setResident(resident);
        complaint.setFlat(resident.getFlat()); // Set the flat from resident
        complaint.setStatus("PENDING");
        complaint.setVendor(null);

        ComplaintEntity savedComplaint = complaintRepo.save(complaint);

        // 🔔 ACTIVITY – Resident
        activityService.logActivity(
                "RESIDENT",
                residentId,
                "COMPLAINT_CREATED",
                "New complaint raised | Complaint ID: " + savedComplaint.getId()
        );

        // 🔔 ACTIVITY – Admin
        activityService.logActivity(
                "ADMIN",
                null,
                "NEW_COMPLAINT",
                "New complaint raised by " + resident.getName() +
                        " | Complaint ID: " + savedComplaint.getId()
        );

        return savedComplaint;
    }

    // ===================== ASSIGN VENDOR =====================

    public ComplaintEntity assignVendor(Long complaintId, Long vendorId) {

        ComplaintEntity complaint = complaintRepo.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        VendorEntity vendor = vendorRepo.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // 🔒 Prevent assigning BUSY / INACTIVE vendors
        if (vendor.getStatus() != VendorStatus.AVAILABLE) {
            throw new RuntimeException("Vendor is not available");
        }

        complaint.setVendor(vendor);
        complaint.setStatus("IN_PROGRESS");

        // 🔥 Mark vendor BUSY
        vendor.setStatus(VendorStatus.BUSY);
        vendorRepo.save(vendor);

        ComplaintEntity savedComplaint = complaintRepo.save(complaint);

        // 🔔 ACTIVITY – Admin
        activityService.logActivity(
                "ADMIN",
                null,
                "VENDOR_ASSIGNED",
                "Vendor " + vendor.getName() +
                        " assigned to Complaint ID " + savedComplaint.getId()
        );

        // 🔔 ACTIVITY – Vendor
        activityService.logActivity(
                "VENDOR",
                vendor.getId(),
                "COMPLAINT_ASSIGNED",
                "New complaint assigned | Complaint ID " + savedComplaint.getId()
        );

        return savedComplaint;
    }

    // ===================== UPDATE STATUS =====================

    public ComplaintEntity updateStatus(Long complaintId, String status) {

        ComplaintEntity complaint = complaintRepo.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setStatus(status);

        VendorEntity vendor = complaint.getVendor();
        ResidentEntity resident = complaint.getResident();

        // ✅ When complaint is completed
        if ("COMPLETED".equalsIgnoreCase(status) && vendor != null) {

            // Free vendor
            vendor.setStatus(VendorStatus.AVAILABLE);
            vendorRepo.save(vendor);

            // 🔔 ACTIVITY – Vendor
            activityService.logActivity(
                    "VENDOR",
                    vendor.getId(),
                    "COMPLAINT_COMPLETED",
                    "Complaint ID " + complaint.getId() + " completed"
            );

            // 🔔 ACTIVITY – Admin
            activityService.logActivity(
                    "ADMIN",
                    null,
                    "COMPLAINT_RESOLVED",
                    "Complaint ID " + complaint.getId() +
                            " resolved by vendor " + vendor.getName()
            );

            // 📧 EMAIL TO RESIDENT
            emailService.sendComplaintResolvedEmail(
                    resident.getEmail(),
                    resident.getName(),
                    complaint.getId()
            );
        }

        // 🔔 ACTIVITY – Status change
        activityService.logActivity(
                "VENDOR",
                vendor != null ? vendor.getId() : null,
                "STATUS_UPDATED",
                "Complaint ID " + complaint.getId() +
                        " status changed to " + status
        );

        return complaintRepo.save(complaint);
    }
    
    public ComplaintEntity completeComplaint(Long complaintId, Double amount) {

        ComplaintEntity complaint = complaintRepo.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        VendorEntity vendor = complaint.getVendor();

        complaint.setStatus("COMPLETED");
        complaint.setServiceAmount(amount);
        complaint.setPaymentDone(false);

        // Free vendor
        vendor.setStatus(VendorStatus.AVAILABLE);
        vendorRepo.save(vendor);

        ComplaintEntity saved = complaintRepo.save(complaint);

        // 🔔 Activity logs
        activityService.logActivity(
            "VENDOR",
            vendor.getId(),
            "COMPLAINT_COMPLETED",
            "Complaint " + saved.getId() +
            " completed. Amount ₹" + amount
        );

        activityService.logActivity(
            "RESIDENT",
            complaint.getResident().getId(),
            "PAYMENT_REQUESTED",
            "Payment requested ₹" + amount +
            " for complaint " + saved.getId()
        );

        return saved;
    }
    
    public void markComplaintPaid(Long complaintId) {

        ComplaintEntity complaint = complaintRepo.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setPaymentDone(true);
        complaintRepo.save(complaint);

        // 💾 Save to MaintenanceBills
        MaintenanceBillEntity bill = new MaintenanceBillEntity();
        bill.setAmount(complaint.getServiceAmount());
        bill.setStatus("Paid");
        bill.setFlat(complaint.getResident().getFlat());
        bill.setDueDate(LocalDateTime.now());

        maintenanceBillRepo.save(bill);

        // 🔔 Activity
        activityService.logActivity(
            "RESIDENT",
            complaint.getResident().getId(),
            "PAYMENT_COMPLETED",
            "Paid ₹" + bill.getAmount() +
            " for complaint " + complaintId
        );

        // 📧 Email
        emailService.sendComplaintResolvedEmail(
            complaint.getResident().getEmail(),
            complaint.getResident().getName(),
            complaintId
        );
    }



    // ===================== READ =====================

    public List<ComplaintEntity> getAllComplaints() {
        return complaintRepo.findAll();
    }

    public List<ComplaintEntity> getComplaintsByResidentId(Long residentId) {
        return complaintRepo.findByResidentId(residentId);
    }

    public List<ComplaintEntity> getComplaintsByVendorId(Long vendorId) {
        return complaintRepo.findByVendorId(vendorId);
    }

    public List<ComplaintEntity> getComplaintsByVendorIdAndStatus(Long vendorId, String status) {
        return complaintRepo.findByVendorIdAndStatus(vendorId, status);
    }

    public Optional<ComplaintEntity> getComplaintById(Long id) {
        return complaintRepo.findById(id);
    }

    // ===================== DELETE =====================

    public void deleteComplaint(Long complaintId) {

        ComplaintEntity complaint = complaintRepo.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        if (!complaint.getStatus().equalsIgnoreCase("PENDING")) {
            throw new RuntimeException("Only pending complaints can be deleted");
        }

        complaint.setStatus("CANCELLED");
        complaintRepo.save(complaint);

        // 🔔 ACTIVITY – Admin
        activityService.logActivity(
                "ADMIN",
                null,
                "COMPLAINT_CANCELLED",
                "Complaint ID " + complaintId + " cancelled by Admin"
        );
    }
}
