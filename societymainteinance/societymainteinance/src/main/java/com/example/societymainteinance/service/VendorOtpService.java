package com.example.societymainteinance.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.societymainteinance.entity.VendorEntity;
import com.example.societymainteinance.repo.VendorRepo;

@Service
public class VendorOtpService {

    @Autowired
    private VendorRepo vendorRepo;

    @Autowired
    private EmailService emailService;

    private final Map<String, String> otpStore = new HashMap<>();
    private final Map<String, Instant> otpExpiry = new HashMap<>();

    // Generate & Send OTP
    public void sendOtp(String email) {

        VendorEntity vendor = vendorRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        otpStore.put(email, otp);
        otpExpiry.put(email, Instant.now().plusSeconds(300)); // 5 mins

        emailService.sendOtpEmail(email, otp);
    }

    // Verify OTP
    public VendorEntity verifyOtp(String email, String otp) {

        if (!otpStore.containsKey(email)) {
            throw new RuntimeException("OTP not requested");
        }

        if (Instant.now().isAfter(otpExpiry.get(email))) {
            otpStore.remove(email);
            otpExpiry.remove(email);
            throw new RuntimeException("OTP expired");
        }

        if (!otpStore.get(email).equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        otpStore.remove(email);
        otpExpiry.remove(email);

        return vendorRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }
}