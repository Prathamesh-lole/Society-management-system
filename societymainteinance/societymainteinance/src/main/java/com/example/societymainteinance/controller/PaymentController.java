package com.example.societymainteinance.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.societymainteinance.entity.ComplaintEntity;
import com.example.societymainteinance.entity.MaintenanceBillEntity;
import com.example.societymainteinance.repo.ComplaintRepo;
import com.example.societymainteinance.repo.MaintenanceBillRepo;
import com.example.societymainteinance.service.PaymentService;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private ComplaintRepo complaintRepo;
    
    @Autowired
    private MaintenanceBillRepo maintenanceBillrepo;

    /**
     * Create Razorpay Order
     * Example:
     * POST /payments/create-order?amount=1200
     */
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestParam Double amount) {
        try {
            System.out.println("=== CREATE ORDER ENDPOINT ===");
            System.out.println("Amount parameter received: " + amount);
            System.out.println("Amount type: " + amount.getClass().getName());
            System.out.println("============================");
            
            return ResponseEntity.ok(paymentService.createOrder(amount));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
    @PutMapping("/pay/{complaintId}")
    public ResponseEntity<ComplaintEntity> markAsPaid(
            @PathVariable Long complaintId) {

        System.out.println("PAY API CALLED for complaint ID: " + complaintId);

        ComplaintEntity complaint = complaintRepo.findById(complaintId)
            .orElseThrow();

        complaint.setPaymentDone(true);
        complaintRepo.save(complaint);

        System.out.println("Complaint marked as paid");

        MaintenanceBillEntity bill = new MaintenanceBillEntity();
        bill.setAmount(complaint.getServiceAmount());
        bill.setDueDate(LocalDateTime.now());
        bill.setStatus("Paid");
        bill.setFlat(complaint.getResident().getFlat());

        maintenanceBillrepo.save(bill);

        System.out.println("Maintenance bill created");

        return ResponseEntity.ok(complaint);
    }

}
