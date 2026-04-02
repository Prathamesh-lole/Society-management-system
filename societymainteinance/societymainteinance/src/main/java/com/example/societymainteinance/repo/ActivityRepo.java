package com.example.societymainteinance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.societymainteinance.entity.ActivityEntity;

@Repository
public interface ActivityRepo extends JpaRepository<ActivityEntity, Long> {

    // Admin dashboard
    List<ActivityEntity> findTop10ByOrderByCreatedAtDesc();

    // Resident dashboard
    List<ActivityEntity> findTop10ByRoleAndUserIdOrderByCreatedAtDesc(
            String role, Long userId);
}

