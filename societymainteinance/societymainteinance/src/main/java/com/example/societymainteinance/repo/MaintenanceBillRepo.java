package com.example.societymainteinance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.societymainteinance.entity.FlatEntity;
import com.example.societymainteinance.entity.MaintenanceBillEntity;

@Repository
public interface MaintenanceBillRepo extends JpaRepository<MaintenanceBillEntity, Long> {

   
    // Optional: Find bills by status
    List<MaintenanceBillEntity> findByStatus(String status);
    
    List<MaintenanceBillEntity> findByFlat_Id(Long flatId);

}