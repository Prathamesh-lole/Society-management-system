package com.example.societymainteinance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.societymainteinance.entity.ActivityEntity;
import com.example.societymainteinance.repo.ActivityRepo;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepo activityRepo;

    /**
     * Common method to log all activities in the system
     *
     * @param role        ADMIN / RESIDENT / VENDOR
     * @param userId      ID of resident/vendor (null for admin)
     * @param action      Action keyword (CREATE, UPDATE, DELETE, ASSIGN, etc.)
     * @param description Human readable activity message
     */
    public void logActivity(String role,
                            Long userId,
                            String action,
                            String description) {

        ActivityEntity activity = new ActivityEntity();
        activity.setRole(role);
        activity.setUserId(userId);
        activity.setAction(action);
        activity.setDescription(description);

        activityRepo.save(activity);
    }

    // -------------------- Dashboard Activities --------------------

    // Admin dashboard → show latest 10 activities
    public List<ActivityEntity> getAdminActivities() {
        return activityRepo.findTop10ByOrderByCreatedAtDesc();
    }

    // Resident dashboard → show resident-specific activities
    public List<ActivityEntity> getResidentActivities(Long residentId) {
        return activityRepo.findTop10ByRoleAndUserIdOrderByCreatedAtDesc(
                "RESIDENT", residentId
        );
    }

    // Vendor dashboard → show vendor-specific activities
    public List<ActivityEntity> getVendorActivities(Long vendorId) {
        return activityRepo.findTop10ByRoleAndUserIdOrderByCreatedAtDesc(
                "VENDOR", vendorId
        );
    }
}
