package com.example.societymainteinance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.societymainteinance.entity.AnnouncementEntity;
import com.example.societymainteinance.service.AnnouncementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/announcements")
@CrossOrigin(origins = "http://localhost:3000")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    // ✅ Admin: Create announcement
    // POST /announcements?adminId=1
    @PostMapping
    public ResponseEntity<AnnouncementEntity> createAnnouncement(
            @RequestParam Long adminId,
            @Valid @RequestBody AnnouncementEntity announcement
    ) {
        return ResponseEntity.ok(
                announcementService.createAnnouncement(adminId, announcement)
        );
    }

    // ✅ Admin: Update announcement
    // PUT /announcements/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AnnouncementEntity> updateAnnouncement(
            @PathVariable Long id,
            @Valid @RequestBody AnnouncementEntity announcement
    ) {
        return ResponseEntity.ok(
                announcementService.updateAnnouncement(id, announcement)
        );
    }

    // ✅ Admin: Delete announcement
    // DELETE /announcements/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok("Announcement deleted successfully");
    }

    // ✅ Resident/Admin: Get all announcements
    // GET /announcements
    @GetMapping
    public ResponseEntity<List<AnnouncementEntity>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }

    // ✅ Get announcement by ID
    // GET /announcements/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementEntity> getAnnouncementById(@PathVariable Long id) {
        return announcementService.getAnnouncementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Admin: Get announcements posted by admin
    // GET /announcements/admin/{adminId}
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<AnnouncementEntity>> getAnnouncementsByAdmin(
            @PathVariable Long adminId
    ) {
        return ResponseEntity.ok(
                announcementService.getAnnouncementsByAdmin(adminId)
        );
    }
}
