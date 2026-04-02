package com.example.societymainteinance.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.societymainteinance.entity.VendorEntity;
import com.example.societymainteinance.enums.VendorStatus;

@Repository
public interface VendorRepo extends JpaRepository<VendorEntity, Long> {

    // Optional: find by email for login or validation
	Optional<VendorEntity> findByEmail(String email);
	
	List<VendorEntity> findByStatus(VendorStatus status);

}