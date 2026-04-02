package com.example.societymainteinance.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.societymainteinance.entity.ResidentEntity;

import java.util.Optional;

@Repository
public interface ResidentRepo extends JpaRepository<ResidentEntity, Long> {
    Optional<ResidentEntity> findByEmail(String email);
    
}