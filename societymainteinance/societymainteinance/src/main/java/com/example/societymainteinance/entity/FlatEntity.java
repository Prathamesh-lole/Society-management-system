package com.example.societymainteinance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "flats")
public class FlatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Flat number cannot be blank")
    private String flatNumber;

    @NotBlank(message = "Block cannot be blank")
    private String block;

    @Min(value = 0, message = "Floor must be 0 or higher")
    private Integer floor;

    @OneToOne(mappedBy = "flat", fetch = FetchType.EAGER)
    private ResidentEntity resident;

    @OneToMany(mappedBy = "flat")
    @JsonIgnore
    private List<MaintenanceBillEntity> bills;
   
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Automatically set timestamps before saving
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Automatically update updatedAt before updating
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFlatNumber() {
		return flatNumber;
	}

	public void setFlatNumber(String flatNumber) {
		this.flatNumber = flatNumber;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public ResidentEntity getResident() {
		return resident;
	}

	public void setResident(ResidentEntity resident) {
		this.resident = resident;
	}

	public List<MaintenanceBillEntity> getBills() {
		return bills;
	}

	public void setBills(List<MaintenanceBillEntity> bills) {
		this.bills = bills;
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
		return "FlatEntity [id=" + id + ", flatNumber=" + flatNumber + ", block=" + block + ", floor=" + floor
				+ ", resident=" + resident + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

	public FlatEntity(Long id, @NotBlank(message = "Flat number cannot be blank") String flatNumber,
			@NotBlank(message = "Block cannot be blank") String block,
			@Min(value = 0, message = "Floor must be 0 or higher") Integer floor, ResidentEntity resident,List<MaintenanceBillEntity> bills,LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.flatNumber = flatNumber;
		this.block = block;
		this.floor = floor;
		this.resident = resident;
		this.bills = bills;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public FlatEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
}