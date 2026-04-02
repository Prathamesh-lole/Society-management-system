package com.example.societymainteinance.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.societymainteinance.entity.VendorEntity;
import com.example.societymainteinance.repo.VendorRepo;
import com.example.societymainteinance.util.OtpUtil;

@Service
public class VendorAuthService {

    @Autowired
    private VendorRepo vendorRepo;

    @Autowired
    private EmailService emailService;

    // 1️⃣ Send OTP
    public String sendOtp(String email) {

        VendorEntity vendor = vendorRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found with this email"));

        String otp = OtpUtil.generateOtp();

        vendor.setOtp(otp);
        vendor.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        vendor.setVerified(false);

        vendorRepo.save(vendor);

        emailService.sendOtpEmail(email, otp);

        return "OTP sent successfully";
    }

    // 2️⃣ Verify OTP
    public VendorEntity verifyOtp(String email, String otp) {

        VendorEntity vendor = vendorRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        if (vendor.getOtp() == null || vendor.getOtpExpiry() == null) {
            throw new RuntimeException("OTP not generated");
        }

        if (LocalDateTime.now().isAfter(vendor.getOtpExpiry())) {
            throw new RuntimeException("OTP expired");
        }

        if (!vendor.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // OTP verified successfully
        vendor.setVerified(true);
        vendor.setOtp(null);
        vendor.setOtpExpiry(null);

        return vendorRepo.save(vendor);
    }
}
