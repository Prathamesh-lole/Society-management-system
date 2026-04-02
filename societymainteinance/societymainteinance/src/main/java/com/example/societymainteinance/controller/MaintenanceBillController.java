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

import com.example.societymainteinance.entity.FlatEntity;
import com.example.societymainteinance.entity.MaintenanceBillEntity;
import com.example.societymainteinance.service.MaintenanceBillService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bills")
public class MaintenanceBillController {

    @Autowired
    private MaintenanceBillService billService;

    // Admin: Create new bill
    @PostMapping
    public ResponseEntity<MaintenanceBillEntity> createBill(@Valid @RequestBody MaintenanceBillEntity bill) {
        return ResponseEntity.ok(billService.createBill(bill));
    }

    // Admin: Update bill
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceBillEntity> updateBill(@PathVariable Long id, @Valid @RequestBody MaintenanceBillEntity bill) {
        return ResponseEntity.ok(billService.updateBill(id, bill));
    }

    // Admin: Delete bill
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.ok("Bill deleted successfully");
    }

    // Admin: Get all bills
    @GetMapping
    public ResponseEntity<List<MaintenanceBillEntity>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }

    // Get bill by ID
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceBillEntity> getBillById(@PathVariable Long id) {
        return billService.getBillById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

 // Resident: Get bills by flat ID
    @GetMapping("/flat/{id}")
    public ResponseEntity<List<MaintenanceBillEntity>> getBillsByFlat(
            @PathVariable Long id) {

        return ResponseEntity.ok(billService.getBillsByFlatId(id));
    }
}