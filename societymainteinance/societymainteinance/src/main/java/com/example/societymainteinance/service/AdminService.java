package com.example.societymainteinance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.societymainteinance.dto.AdminUpdateRequest;
import com.example.societymainteinance.entity.AdminEntity;
import com.example.societymainteinance.repo.AdminRepo;

@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private ActivityService activityService;

    // Create admin
    public AdminEntity createAdmin(AdminEntity admin) {
        return adminRepo.save(admin);
    }

    // Get all admins
    public List<AdminEntity> getAllAdmins() {
        return adminRepo.findAll();
    }

    // Get admin by ID
    public Optional<AdminEntity> getAdminById(Long id) {
        return adminRepo.findById(id);
    }

    // Update admin (Profile Update)
    public AdminEntity updateAdmin(Long id, AdminEntity updatedAdmin) {

        AdminEntity admin = adminRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id " + id));

        admin.setName(updatedAdmin.getName());
        admin.setEmail(updatedAdmin.getEmail());
        admin.setPhone(updatedAdmin.getPhone());

        // Update password only if provided
        if (updatedAdmin.getPassword() != null &&
            !updatedAdmin.getPassword().isEmpty()) {
            admin.setPassword(updatedAdmin.getPassword());
        }

        AdminEntity savedAdmin = adminRepo.save(admin);

        // 🔥 Activity log
        activityService.logActivity(
                "ADMIN",
                id,
                "UPDATE",
                "Admin profile updated"
        );

        return savedAdmin;
    }
    
    public AdminEntity updateAdminProfile(Long id, AdminUpdateRequest request) {

        AdminEntity admin = adminRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Update basic fields
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());

        // 🔐 PASSWORD CHANGE LOGIC
        if (request.getNewPassword() != null &&
            !request.getNewPassword().isEmpty()) {

            // verify current password
            if (!admin.getPassword().equals(request.getCurrentPassword())) {
                throw new RuntimeException("Current password is incorrect");
            }

            admin.setPassword(request.getNewPassword());
        }

        return adminRepo.save(admin);
    }

    // Delete admin
    public void deleteAdmin(Long id) {
        adminRepo.deleteById(id);
    }
}
