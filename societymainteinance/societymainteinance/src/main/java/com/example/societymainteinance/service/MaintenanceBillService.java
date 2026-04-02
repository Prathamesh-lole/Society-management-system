package com.example.societymainteinance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.societymainteinance.entity.FlatEntity;
import com.example.societymainteinance.entity.MaintenanceBillEntity;
import com.example.societymainteinance.repo.MaintenanceBillRepo;

@Service
public class MaintenanceBillService {

    @Autowired
    private MaintenanceBillRepo billRepo;

    // Create bill (Admin)
    public MaintenanceBillEntity createBill(MaintenanceBillEntity bill) {
        return billRepo.save(bill);
    }

    // Update bill (Admin)
    public MaintenanceBillEntity updateBill(Long id, MaintenanceBillEntity billDetails) {
        MaintenanceBillEntity bill = billRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found with id " + id));

        bill.setAmount(billDetails.getAmount());
        bill.setDueDate(billDetails.getDueDate());
        bill.setStatus(billDetails.getStatus());
        bill.setFlat(billDetails.getFlat());

        return billRepo.save(bill);
    }

    // Delete bill
    public void deleteBill(Long id) {
        billRepo.deleteById(id);
    }

    // Get all bills
    public List<MaintenanceBillEntity> getAllBills() {
        return billRepo.findAll();
    }

    // Get bill by ID
    public Optional<MaintenanceBillEntity> getBillById(Long id) {
        return billRepo.findById(id);
    }

 // Get bills by flat ID (Resident)
    public List<MaintenanceBillEntity> getBillsByFlatId(Long flatId) {
        return billRepo.findByFlat_Id(flatId);
    }

}