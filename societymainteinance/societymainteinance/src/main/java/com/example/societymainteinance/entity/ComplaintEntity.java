package com.example.societymainteinance.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
public class ComplaintEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    private String status; // e.g., "Pending", "In Progress", "Resolved" - Set by service layer

    // Many complaints can belong to one resident
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resident_id", nullable = false)
    @JsonIgnoreProperties({"complaints", "flat", "password"})
    private ResidentEntity resident;

    // Many complaints can belong to one flat
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flat_id", nullable = true)
    @JsonIgnoreProperties({"residents", "bills"})
    private FlatEntity flat;

    // Many complaints can be assigned to one vendor
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_id")
    @JsonIgnoreProperties({"complaints"})
    private VendorEntity vendor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Timestamps
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.paymentDone = false;   // 🔥 IMPORTANT

    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    
    private Double serviceAmount;   // amount entered by vendor
    private Boolean paymentDone;    // false by default
    
   
    
    @Column
    private Double amount;

    public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	// Constructors
    public ComplaintEntity() {
    }

    public ComplaintEntity(Long id, String title, String description, String status, ResidentEntity resident, VendorEntity vendor,LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.resident = resident;
        this.vendor = vendor;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Double getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(Double serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

	public Boolean getPaymentDone() {
		return paymentDone;
	}

	public void setPaymentDone(Boolean paymentDone) {
		this.paymentDone = paymentDone;
	}

	// Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public ResidentEntity getResident() { return resident; }
    public void setResident(ResidentEntity resident) { this.resident = resident; }

    public FlatEntity getFlat() { return flat; }
    public void setFlat(FlatEntity flat) { this.flat = flat; }

    public VendorEntity getVendor() { return vendor; }
    public void setVendor(VendorEntity vendor) { this.vendor = vendor; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
