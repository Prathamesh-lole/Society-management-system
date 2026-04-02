package com.example.societymainteinance.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.societymainteinance.entity.AnnouncementEntity;
import java.util.List;

@Repository
public interface AnnouncementRepo extends JpaRepository<AnnouncementEntity, Long> {

    // Optional: Get announcements by Admin
    List<AnnouncementEntity> findByAdminId(Long adminId);
}