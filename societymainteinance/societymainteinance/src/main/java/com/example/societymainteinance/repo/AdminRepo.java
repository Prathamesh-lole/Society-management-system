package com.example.societymainteinance.repo;

import com.example.societymainteinance.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepo extends JpaRepository<AdminEntity, Long> {
    Optional<AdminEntity> findByEmail(String email);
    
    
}
