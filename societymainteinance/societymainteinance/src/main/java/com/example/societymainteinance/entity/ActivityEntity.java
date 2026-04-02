package com.example.societymainteinance.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "activities")
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role; 
    // ADMIN, RESIDENT, VENDOR

    private Long userId; 
    // residentId / vendorId / adminId (optional)

    private String action; 
    // "Complaint Raised", "Vendor Assigned", etc.

    private String description; 
    // Detailed message

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "ActivityEntity [id=" + id + ", role=" + role + ", userId=" + userId + ", action=" + action
				+ ", description=" + description + ", createdAt=" + createdAt + "]";
	}

	public ActivityEntity(Long id, String role, Long userId, String action, String description,
			LocalDateTime createdAt) {
		super();
		this.id = id;
		this.role = role;
		this.userId = userId;
		this.action = action;
		this.description = description;
		this.createdAt = createdAt;
	}

	public ActivityEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

    
    
}
