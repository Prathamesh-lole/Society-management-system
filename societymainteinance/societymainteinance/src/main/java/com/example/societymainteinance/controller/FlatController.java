package com.example.societymainteinance.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.societymainteinance.entity.FlatEntity;
import com.example.societymainteinance.entity.ResidentEntity;
import com.example.societymainteinance.repo.ResidentRepo;
import com.example.societymainteinance.service.FlatService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/flats")
@CrossOrigin(origins = "*") // Allow frontend access
public class FlatController {

    @Autowired
    private FlatService flatService;

    @Autowired
    private ResidentRepo residentRepo;

    // -------------------- CRUD Operations --------------------

    // Create flat
    @PostMapping
    public ResponseEntity<FlatEntity> createFlat(@Valid @RequestBody FlatEntity flat) {
        FlatEntity created = flatService.createFlat(flat);
        return ResponseEntity.ok(created);
    }

    // Get all flats
    @GetMapping
    public ResponseEntity<List<FlatEntity>> getAllFlats() {
        // This should fetch all flats
        List<FlatEntity> flats = flatService.getAllFlats(); 
        return ResponseEntity.ok(flats);
    }

    // Get flat by ID
    @GetMapping("/{id}")
    public ResponseEntity<FlatEntity> getFlatById(@PathVariable Long id) {
        return flatService.getFlatById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update flat
    @PutMapping("/{id}")
    public ResponseEntity<FlatEntity> updateFlat(@PathVariable Long id,
                                                 @Valid @RequestBody FlatEntity flat) {
        FlatEntity updated = flatService.updateFlat(id, flat);
        return ResponseEntity.ok(updated);
    }

    // Delete flat
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFlat(@PathVariable Long id) {
        flatService.deleteFlat(id);
        return ResponseEntity.ok("Flat deleted successfully");
    }

    // -------------------- Assign Resident --------------------

    /**
     * Assign a resident to a flat (bidirectional)
     * Example: PUT /flats/assign-resident?flatId=1&residentId=2
     */
    @PutMapping("/assign-resident")
    public ResponseEntity<List<ResidentEntity>> assignResidentToFlat(
            @RequestParam Long flatId,
            @RequestParam Long residentId) {

        flatService.assignResidentToFlat(flatId, residentId, residentRepo);

        List<ResidentEntity> allResidents = residentRepo.findAll();
        return ResponseEntity.ok(allResidents);
    }

    // -------------------- Search Flats --------------------

    /**
     * Search flats by flat number or block
     * Example: GET /flats/search?query=A101
     */
    @GetMapping("/search")
    public ResponseEntity<List<FlatEntity>> searchFlats(@RequestParam String query) {
        List<FlatEntity> results = flatService.searchFlats(query);
        return ResponseEntity.ok(results);
    }
}