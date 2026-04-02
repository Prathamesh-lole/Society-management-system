package com.example.societymainteinance.repo;

import com.example.societymainteinance.entity.ComplaintEntity;
import com.example.societymainteinance.entity.ResidentEntity;
import com.example.societymainteinance.entity.VendorEntity;

//import com.example.societymainteinance.entity.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepo extends JpaRepository<ComplaintEntity, Long> {

    // Resident
    List<ComplaintEntity> findByResident(ResidentEntity resident);

    List<ComplaintEntity> findByResidentId(Long residentId);

    // Vendor
    List<ComplaintEntity> findByVendorId(Long vendorId);

    // 🔥 THIS IS THE MISSING METHOD (IMPORTANT)
    List<ComplaintEntity> findByVendorIdAndStatus(Long vendorId, String status);

    // Optional
    List<ComplaintEntity> findByStatus(String status);
    
    long countByStatus(String status);
    long countByVendorId(Long vendorId);
    long countByVendorIdAndStatus(Long vendorId, String status);

}