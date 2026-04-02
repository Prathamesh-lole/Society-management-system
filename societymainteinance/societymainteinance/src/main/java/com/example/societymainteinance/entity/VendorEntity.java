package com.example.societymainteinance.entity;

import com.example.societymainteinance.enums.VendorStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vendors")
public class VendorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vendor name cannot be blank")
    private String name;

    @NotBlank(message = "Service type cannot be blank")
    private String serviceType;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String email;
    
    public VendorStatus getStatus() {
		return status;
	}

	public void setStatus(VendorStatus status) {
		this.status = status;
	}

	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VendorStatus status = VendorStatus.AVAILABLE;

    // One vendor can have multiple complaints assigned
    @OneToMany(mappedBy = "vendor")
    @JsonIgnore
    private List<ComplaintEntity> complaints;

    // ================= OTP LOGIN FIELDS =================

    @Column(length = 6)
    private String otp;

    private LocalDateTime otpExpiry;

    private Boolean verified = false;

    // ================= AUDIT FIELDS =================

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ================= LIFECYCLE CALLBACKS =================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ================= CONSTRUCTORS =================

    public VendorEntity() {
    }

    public VendorEntity(Long id, String name, String serviceType, String phone, String email,
                        List<ComplaintEntity> complaints, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.serviceType = serviceType;
        this.phone = phone;
        this.email = email;
        this.complaints = complaints;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ComplaintEntity> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<ComplaintEntity> complaints) {
        this.complaints = complaints;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ================= OTP GETTERS & SETTERS =================

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
