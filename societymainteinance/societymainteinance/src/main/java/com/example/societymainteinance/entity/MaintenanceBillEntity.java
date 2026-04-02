package com.example.societymainteinance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_bills")
public class MaintenanceBillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Amount must be positive
    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;

    @NotNull
    private String status; // Pending / Paid

    // Many bills can belong to one flat
    @ManyToOne
    @JoinColumn(name = "flat_id", nullable = false)
    @JsonIgnore
    private FlatEntity flat;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Timestamps
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (status == null) status = "Pending";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Constructors
    public MaintenanceBillEntity() {}

    public MaintenanceBillEntity(Long id, Double amount, LocalDateTime dueDate, String status, FlatEntity flat, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = status;
        this.flat = flat;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public FlatEntity getFlat() { return flat; }
    public void setFlat(FlatEntity flat) { this.flat = flat; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}