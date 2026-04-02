package com.example.societymainteinance.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.societymainteinance.entity.AnnouncementEntity;
import com.example.societymainteinance.entity.ComplaintEntity;
import com.example.societymainteinance.entity.FlatEntity;
import com.example.societymainteinance.entity.ResidentEntity;
import com.example.societymainteinance.service.AnnouncementService;
import com.example.societymainteinance.service.ComplaintService;
import com.example.societymainteinance.service.ResidentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/residents")
public class ResidentController {

    @Autowired
    private ResidentService residentService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private AnnouncementService announcementService;

    // ===================== RESIDENT CRUD =====================

    // Create Resident (Registration + welcome email)
    @PostMapping
    public ResponseEntity<ResidentEntity> createResident(
            @Valid @RequestBody ResidentEntity resident) {

        ResidentEntity created = residentService.registerResident(resident);
        return ResponseEntity.ok(created);
    }
    

    // Get all residents
    @GetMapping
    public ResponseEntity<List<ResidentEntity>> getAllResidents() {
        return ResponseEntity.ok(residentService.getAllResidents());
    }

    // Get resident by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResidentEntity> getResidentById(@PathVariable Long id) {
        return residentService.getResidentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update resident
    @PutMapping("/{id}")
    public ResponseEntity<ResidentEntity> updateResident(
            @PathVariable Long id,
            @Valid @RequestBody ResidentEntity resident) {

        ResidentEntity updated = residentService.updateResident(id, resident);
        return ResponseEntity.ok(updated);
    }

    // Delete resident
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResident(@PathVariable Long id) {
        residentService.deleteResident(id);
        return ResponseEntity.ok("Resident deleted successfully");
    }
    
    

    // ===================== FLAT =====================

    // Get resident's flat info
    @GetMapping("/{residentId}/flat")
    public ResponseEntity<FlatEntity> getFlat(@PathVariable Long residentId) {
        FlatEntity flat = residentService.getFlatByResidentId(residentId);

        if (flat == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(flat);
    }

    // ===================== COMPLAINTS =====================

    // Raise a complaint
    @PostMapping("/{residentId}/complaints")
    public ResponseEntity<String> raiseComplaint(
            @PathVariable Long residentId,
            @RequestBody ComplaintEntity complaint) {

        complaintService.createComplaint(residentId, complaint);
        return ResponseEntity.ok("Complaint raised successfully");
    }

    // Get resident's complaints
    @GetMapping("/{residentId}/complaints")
    public ResponseEntity<List<ComplaintEntity>> getOwnComplaints(
            @PathVariable Long residentId) {

        return ResponseEntity.ok(
                complaintService.getComplaintsByResidentId(residentId)
        );
    }

    // Get updated complaints since timestamp
    @GetMapping("/{residentId}/complaints/updates")
    public ResponseEntity<List<ComplaintEntity>> getUpdatedComplaints(
            @PathVariable Long residentId,
            @RequestParam(required = false) String since) {

        List<ComplaintEntity> complaints =
                complaintService.getComplaintsByResidentId(residentId);

        if (since != null && !since.isEmpty()) {
            LocalDateTime lastCheck = LocalDateTime.parse(since);

            complaints = complaints.stream()
                    .filter(c ->
                            c.getUpdatedAt() != null &&
                            c.getUpdatedAt().isAfter(lastCheck))
                    .toList();
        }

        return ResponseEntity.ok(complaints);
    }

    // ===================== ANNOUNCEMENTS =====================

    // Get all announcements
    @GetMapping("/announcements")
    public ResponseEntity<List<AnnouncementEntity>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }
}
