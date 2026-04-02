package com.example.societymainteinance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.societymainteinance.entity.FlatEntity;
import com.example.societymainteinance.entity.ResidentEntity;
import com.example.societymainteinance.repo.FlatRepo;
import com.example.societymainteinance.repo.ResidentRepo;

@Service
public class ResidentService {

    @Autowired
    private ResidentRepo residentRepo;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private FlatRepo flatRepo;


    // ===================== CREATE / REGISTER =====================

    // Create resident + send welcome email
    
    public ResidentEntity registerResident(ResidentEntity resident) {

        ResidentEntity savedResident = residentRepo.save(resident);

        // 🔔 LOG ACTIVITY FOR ADMIN
        activityService.logActivity(
        	    "ADMIN",
        	    null,
        	    "RESIDENT_REGISTERED",
        	    "New resident registered: " + savedResident.getName()
        	);

        emailService.sendWelcomeEmail(
            savedResident.getEmail(),
            savedResident.getName()
        );

        return savedResident;
    }
    
    
    public ResidentEntity createResident(ResidentEntity resident) {

        ResidentEntity savedResident = residentRepo.save(resident);

        // Send welcome email
        emailService.sendWelcomeEmail(
                savedResident.getEmail(),
                savedResident.getName()
        );

        return savedResident;
    }

    // ===================== READ =====================

    // Get all residents
    public List<ResidentEntity> getAllResidents() {
        return residentRepo.findAll();
    }

    // Get resident by ID
    public Optional<ResidentEntity> getResidentById(Long id) {
        return residentRepo.findById(id);
    }

    // Get flat by resident ID
    public FlatEntity getFlatByResidentId(Long residentId) {
        return residentRepo.findById(residentId)
                .map(ResidentEntity::getFlat)
                .orElse(null);
    }

    // ===================== UPDATE =====================

    // Update resident
    public ResidentEntity updateResident(Long id, ResidentEntity updatedResident) {

        return residentRepo.findById(id).map(resident -> {

            resident.setName(updatedResident.getName());
            resident.setEmail(updatedResident.getEmail());
            resident.setPhone(updatedResident.getPhone());
            resident.setPassword(updatedResident.getPassword());
            // resident.setFlat(updatedResident.getFlat()); // optional

            return residentRepo.save(resident);

        }).orElseThrow(() ->
                new RuntimeException("Resident not found with id " + id)
        );
    }

    // ===================== DELETE =====================

    // Delete resident (flat will be automatically unassigned via @PreRemove callback)
    public void deleteResident(Long id) {
        ResidentEntity resident = residentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Resident not found with id " + id));
        
        String residentName = resident.getName();
        String flatInfo = resident.getFlat() != null ? 
            " (Flat " + resident.getFlat().getFlatNumber() + " will be unassigned)" : "";
        
        // Delete the resident - @PreRemove will handle flat unassignment
        residentRepo.deleteById(id);
        
        // 🔔 LOG ACTIVITY FOR ADMIN
        activityService.logActivity(
            "ADMIN",
            null,
            "RESIDENT_DELETED",
            "Resident deleted: " + residentName + flatInfo
        );
    }
    
    
}
