package com.example.societymainteinance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.societymainteinance.dto.AdminUpdateRequest;
import com.example.societymainteinance.entity.AdminEntity;
import com.example.societymainteinance.entity.ComplaintEntity;
import com.example.societymainteinance.entity.FlatEntity;
import com.example.societymainteinance.entity.ResidentEntity;
import com.example.societymainteinance.entity.VendorEntity;
import com.example.societymainteinance.repo.ComplaintRepo;
import com.example.societymainteinance.repo.FlatRepo;
import com.example.societymainteinance.repo.ResidentRepo;
import com.example.societymainteinance.repo.VendorRepo;
import com.example.societymainteinance.service.AdminService;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ResidentRepo residentRepo;

    @Autowired
    private VendorRepo vendorRepo;

    @Autowired
    private ComplaintRepo complaintRepo;

    @Autowired
    private FlatRepo flatRepo;
    
    
    // Create admin
    @PostMapping("/add")
    public ResponseEntity<AdminEntity> createAdmin(@RequestBody AdminEntity admin) {
        AdminEntity created = adminService.createAdmin(admin);
        return ResponseEntity.ok(created);
    }

    // Get all admins
    @GetMapping("/get")
    public ResponseEntity<List<AdminEntity>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    // Get admin by ID
    @GetMapping("/{id}")
    public ResponseEntity<AdminEntity> getAdminById(@PathVariable Long id) {
        return adminService.getAdminById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update admin
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(
            @PathVariable Long id,
            @RequestBody AdminUpdateRequest request) {

        AdminEntity updatedAdmin = adminService.updateAdminProfile(id, request);
        return ResponseEntity.ok(updatedAdmin);
    }

    // Delete admin
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok("Admin deleted successfully");
    }
    
    
 // 1️⃣ Get all residents
    @GetMapping("/residents")
    public ResponseEntity<List<ResidentEntity>> getAllResidents() {
        List<ResidentEntity> residents = residentRepo.findAll();
        return ResponseEntity.ok(residents);
    }

    // 2️⃣ Get all vendors
    @GetMapping("/vendors")
    public ResponseEntity<List<VendorEntity>> getAllVendors() {
        List<VendorEntity> vendors = vendorRepo.findAll();
        return ResponseEntity.ok(vendors);
    }

    // 3️⃣ Get all complaints
    @GetMapping("/complaints")
    public ResponseEntity<List<ComplaintEntity>> getAllComplaints() {
        List<ComplaintEntity> complaints = complaintRepo.findAll();
        return ResponseEntity.ok(complaints);
    }

    // 4️⃣ Assign vendor to a complaint
    @PutMapping("/complaints/{complaintId}/assign/{vendorId}")
    public ResponseEntity<String> assignVendor(
            @PathVariable Long complaintId,
            @PathVariable Long vendorId) {

        ComplaintEntity complaint = complaintRepo.findById(complaintId).orElse(null);
        VendorEntity vendor = vendorRepo.findById(vendorId).orElse(null);

        if (complaint == null || vendor == null) {
            return ResponseEntity.badRequest().body("Complaint or Vendor not found");
        }

        complaint.setVendor(vendor);
        complaint.setStatus("Assigned");
        complaintRepo.save(complaint);

        return ResponseEntity.ok("Vendor assigned to complaint successfully");
    }

    // 5️⃣ Optional: get all flats
    @GetMapping("/flats")
    public ResponseEntity<List<FlatEntity>> getAllFlats() {
        List<FlatEntity> flats = flatRepo.findAll();
        return ResponseEntity.ok(flats);
    }
}
