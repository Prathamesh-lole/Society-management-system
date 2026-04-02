package com.example.societymainteinance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.societymainteinance.entity.AdminEntity;
import com.example.societymainteinance.entity.AnnouncementEntity;
import com.example.societymainteinance.repo.AdminRepo;
import com.example.societymainteinance.repo.AnnouncementRepo;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepo announcementRepo;
    
    @Autowired
    private AdminRepo adminRepo;

    // Create announcement (Admin)
    public AnnouncementEntity createAnnouncement(Long adminId, AnnouncementEntity announcement) {

        AdminEntity admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        announcement.setAdmin(admin);

        return announcementRepo.save(announcement);
    }

    // Update announcement (Admin)
    public AnnouncementEntity updateAnnouncement(Long id, AnnouncementEntity details) {
        AnnouncementEntity announcement = announcementRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id " + id));

        announcement.setTitle(details.getTitle());
        announcement.setContent(details.getContent());

        return announcementRepo.save(announcement);
    }

    // Delete announcement (Admin)
    public void deleteAnnouncement(Long id) {
        announcementRepo.deleteById(id);
    }

    // Get all announcements
    public List<AnnouncementEntity> getAllAnnouncements() {
        return announcementRepo.findAll();
    }

    // Get announcement by ID
    public Optional<AnnouncementEntity> getAnnouncementById(Long id) {
        return announcementRepo.findById(id);
    }

    // Get announcements by Admin
    public List<AnnouncementEntity> getAnnouncementsByAdmin(Long adminId) {
        return announcementRepo.findByAdminId(adminId);
    }
}