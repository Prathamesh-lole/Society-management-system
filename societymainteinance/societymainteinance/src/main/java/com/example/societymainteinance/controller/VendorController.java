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
import org.springframework.web.bind.annotation.RestController;

import com.example.societymainteinance.entity.VendorEntity;
import com.example.societymainteinance.repo.ComplaintRepo;
import com.example.societymainteinance.service.VendorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;
    
    @Autowired
    private ComplaintRepo complaintRepo;
    

    // Admin: Create new vendor
    @PostMapping
    public ResponseEntity<VendorEntity> createVendor(@Valid @RequestBody VendorEntity vendor) {
        VendorEntity created = vendorService.createVendor(vendor);
        return ResponseEntity.ok(created);
    }

    // Admin: Update vendor info
    @PutMapping("/{id}")
    public ResponseEntity<VendorEntity> updateVendor(@PathVariable Long id, @Valid @RequestBody VendorEntity vendor) {
        VendorEntity updated = vendorService.updateVendor(id, vendor);
        return ResponseEntity.ok(updated);
    }

    // Admin: Delete vendor
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
        return ResponseEntity.ok("Vendor deleted successfully");
    }
    
    
    
    @GetMapping("/available")
    public List<VendorEntity> getAvailableVendors() {
        return vendorService.getAvailableVendors();
    }

    // Admin or any: Get vendor by ID
    @GetMapping("/{id}")
    public ResponseEntity<VendorEntity> getVendorById(@PathVariable Long id) {
        return vendorService.getVendorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Admin: Get all vendors
    @GetMapping
    public ResponseEntity<List<VendorEntity>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }
    
    @GetMapping("/{id}/summary")
    public Map<String, Long> vendorSummary(@PathVariable Long id) {

        return Map.of(
            "assigned", complaintRepo.countByVendorId(id),
            "completed", complaintRepo.countByVendorIdAndStatus(id, "COMPLETED")
        );
    }
}