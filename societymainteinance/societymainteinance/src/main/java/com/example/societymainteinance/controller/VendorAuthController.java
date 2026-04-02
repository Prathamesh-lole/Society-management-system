package com.example.societymainteinance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.societymainteinance.entity.VendorEntity;
import com.example.societymainteinance.service.VendorAuthService;

@RestController
@RequestMapping("/vendor/auth")
@CrossOrigin(origins = "*")
public class VendorAuthController {

    @Autowired
    private VendorAuthService vendorAuthService;

    // 1️⃣ Send OTP to vendor email
    // POST /vendor/auth/send-otp?email=abc@gmail.com
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        String response = vendorAuthService.sendOtp(email);
        return ResponseEntity.ok(response);
    }

    // 2️⃣ Verify OTP
    // POST /vendor/auth/verify-otp?email=abc@gmail.com&otp=123456
    @PostMapping("/verify-otp")
    public ResponseEntity<VendorEntity> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {

        VendorEntity vendor = vendorAuthService.verifyOtp(email, otp);
        return ResponseEntity.ok(vendor);
    }
}
