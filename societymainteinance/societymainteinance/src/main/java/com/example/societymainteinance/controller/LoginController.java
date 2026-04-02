package com.example.societymainteinance.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.societymainteinance.dto.LoginRequest;
import com.example.societymainteinance.entity.AdminEntity;
import com.example.societymainteinance.entity.ResidentEntity;
import com.example.societymainteinance.repo.AdminRepo;
import com.example.societymainteinance.repo.ResidentRepo;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private ResidentRepo residentRepo;

 // Admin login
    @PostMapping("/admin-login")
    public ResponseEntity<String> adminLogin(@RequestBody LoginRequest request) {
        Optional<AdminEntity> adminOpt = adminRepo.findByEmail(request.getEmail());

        if (adminOpt.isPresent() && adminOpt.get().getPassword().equals(request.getPassword())) {
            return ResponseEntity.ok("Admin login successful");
        }
        return ResponseEntity.status(401).body("Invalid admin credentials");
    }

    // Resident login
    @PostMapping("/resident-login")
    public ResponseEntity<String> residentLogin(@RequestBody LoginRequest request) {
        Optional<ResidentEntity> residentOpt = residentRepo.findByEmail(request.getEmail());

        if (residentOpt.isPresent() && residentOpt.get().getPassword().equals(request.getPassword())) {
            return ResponseEntity.ok("Resident login successful");
        }
        return ResponseEntity.status(401).body("Invalid resident credentials");
    }
}

