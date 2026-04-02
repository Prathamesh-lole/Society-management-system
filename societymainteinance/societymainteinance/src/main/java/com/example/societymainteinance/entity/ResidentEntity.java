package com.example.societymainteinance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "residents")
public class ResidentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
        message = "Password must have uppercase, lowercase, number, and special character"
    )
    private String password;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(
        regexp = "^[0-9]{10}$",
        message = "Phone number must be 10 digits"
    )
    private String phone;

    @OneToOne
    @JoinColumn(name = "flat_id", foreignKey = @ForeignKey(name = "fk_resident_flat"))
    @JsonIgnore
    private FlatEntity flat;

    @OneToMany(mappedBy = "resident")
    @JsonIgnore
    private List<ComplaintEntity> complaints;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreRemove
    public void onDelete() {
        // Automatically unassign flat when resident is deleted
        if (this.flat != null) {
            this.flat.setResident(null);
        }
    }
    
    
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public FlatEntity getFlat() {
		return flat;
	}
	public void setFlat(FlatEntity flat) {
		this.flat = flat;
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
	
	
	
	@Override
	public String toString() {
		return "ResidentEntity [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password
				+ ", phone=" + phone + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
	
	public ResidentEntity(Long id, @NotBlank(message = "Name cannot be blank") String name,
			@Email(message = "Email should be valid") String email,
			@NotBlank(message = "Password cannot be blank") @Size(min = 8, message = "Password must be at least 8 characters") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$", message = "Password must have uppercase, lowercase, number, and special character") String password,
			@NotBlank(message = "Phone number cannot be blank") @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits") String phone,
			FlatEntity flat, List<ComplaintEntity> complaints, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.flat = flat;
		this.complaints = complaints;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	
	public ResidentEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

   
    
    
}
