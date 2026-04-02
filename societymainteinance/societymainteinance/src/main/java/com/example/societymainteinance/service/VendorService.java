package com.example.societymainteinance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.societymainteinance.entity.VendorEntity;
import com.example.societymainteinance.enums.VendorStatus;
import com.example.societymainteinance.repo.VendorRepo;

@Service
public class VendorService {

    @Autowired
    private VendorRepo vendorRepo;
    
    @Autowired
    private ActivityService activityService;

    // Create or add new vendor (Admin)
    public VendorEntity createVendor(VendorEntity vendor) {
        return vendorRepo.save(vendor);
    }

    // Update vendor info (Admin)
    public VendorEntity updateVendor(Long id, VendorEntity vendorDetails) {

        VendorEntity vendor = vendorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id " + id));

        vendor.setName(vendorDetails.getName());
        vendor.setServiceType(vendorDetails.getServiceType());
        vendor.setPhone(vendorDetails.getPhone());
        vendor.setEmail(vendorDetails.getEmail());

        VendorEntity savedVendor = vendorRepo.save(vendor);

        // 📝 Activity Log
        activityService.logActivity(
            "ADMIN",
            null,
            "VENDOR_UPDATED",
            "Vendor details updated: " + savedVendor.getName()
        );

        return savedVendor;
    }


    
    public List<VendorEntity> getAvailableVendors() {
        return vendorRepo.findByStatus(VendorStatus.AVAILABLE);
    }
    
    
    // Delete vendor (Admin)
    public void deleteVendor(Long id) {
        vendorRepo.deleteById(id);
    }

    // Get vendor by ID
    public Optional<VendorEntity> getVendorById(Long id) {
        return vendorRepo.findById(id);
    }

    // Get all vendors (Admin)
    public List<VendorEntity> getAllVendors() {
        return vendorRepo.findAll();
    }
}