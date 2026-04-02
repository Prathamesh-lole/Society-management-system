package com.example.societymainteinance.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.societymainteinance.entity.ComplaintEntity;
import com.example.societymainteinance.entity.ResidentEntity;
import com.example.societymainteinance.entity.VendorEntity;
import com.example.societymainteinance.repo.ComplaintRepo;
import com.example.societymainteinance.service.ComplaintService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;
    
    @Autowired
    private ComplaintRepo complaintRepo;

    // Resident: Raise complaint
    @PostMapping
    public ResponseEntity<ComplaintEntity> createComplaint(
            @RequestParam Long residentId,
            @Valid @RequestBody ComplaintEntity complaint
    ) {
        return ResponseEntity.ok(
                complaintService.createComplaint(residentId, complaint)
        );
    }

    // Admin: Assign vendor
    @PutMapping("/{complaintId}/assign")
    public ResponseEntity<ComplaintEntity> assignVendor(
            @PathVariable Long complaintId,
            @RequestParam Long vendorId
    ) {
        return ResponseEntity.ok(
                complaintService.assignVendor(complaintId, vendorId)
        );
    }

    // Vendor: Update status
    @PutMapping("/{id}/status")
    public ResponseEntity<ComplaintEntity> updateStatus(@PathVariable Long id, @RequestParam String status) {
        ComplaintEntity updated = complaintService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    // Admin: Get all complaints
    @GetMapping
    public ResponseEntity<List<ComplaintEntity>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    // Resident: Get complaints by resident
    @GetMapping("/resident/{id}")
    public ResponseEntity<List<ComplaintEntity>> getByResident(@PathVariable Long id) {
        return ResponseEntity.ok(
                complaintService.getComplaintsByResidentId(id)
        );
    }

    // Vendor: Get complaints by vendor
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<ComplaintEntity>> getByVendor(
            @PathVariable Long vendorId,
            @RequestParam(required = false) String status
    ) {
        if (status != null && !status.isEmpty()) {
            return ResponseEntity.ok(
                    complaintService.getComplaintsByVendorIdAndStatus(vendorId, status)
            );
        }

        return ResponseEntity.ok(
                complaintService.getComplaintsByVendorId(vendorId)
        );
    }

    // Get complaint by ID
    @GetMapping("/{id}")
    public ResponseEntity<ComplaintEntity> getById(@PathVariable Long id) {
        return complaintService.getComplaintById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete complaint
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.ok("Complaint deleted successfully");
    }
    
    @PutMapping("/complete/{id}")
    public ResponseEntity<ComplaintEntity> completeComplaint(
            @PathVariable Long id,
            @RequestParam Double amount) {

        return ResponseEntity.ok(
            complaintService.completeComplaint(id, amount)
        );
    }
    
    @PutMapping("/mark-paid/{id}")
    public void markPaid(@PathVariable Long id) {
        complaintService.markComplaintPaid(id);
    }

    
    @PutMapping("/vendor/{complaintId}/amount")
    public ResponseEntity<ComplaintEntity> addServiceAmount(
            @PathVariable Long complaintId,
            @RequestParam Double amount) {

        System.out.println("=== ADD SERVICE AMOUNT ===");
        System.out.println("Complaint ID: " + complaintId);
        System.out.println("Amount received: " + amount);
        
        ComplaintEntity complaint = complaintRepo.findById(complaintId)
            .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setServiceAmount(amount);
        complaint.setStatus("Completed");

        ComplaintEntity saved = complaintRepo.save(complaint);
        
        System.out.println("Service amount saved: " + saved.getServiceAmount());
        System.out.println("========================");

        return ResponseEntity.ok(saved);
    }

    
    @GetMapping("/count")
    public Map<String, Long> getComplaintCounts() {

        return Map.of(
            "PENDING", complaintRepo.countByStatus("PENDING"),
            "IN_PROGRESS", complaintRepo.countByStatus("IN_PROGRESS"),
            "COMPLETED", complaintRepo.countByStatus("COMPLETED")
        );
    }
}