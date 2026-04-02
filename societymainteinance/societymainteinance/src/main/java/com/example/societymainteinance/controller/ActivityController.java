package com.example.societymainteinance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.societymainteinance.entity.ActivityEntity;
import com.example.societymainteinance.service.ActivityService;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/admin")
    public List<ActivityEntity> adminActivities() {
        return activityService.getAdminActivities();
    }

    @GetMapping("/resident/{id}")
    public List<ActivityEntity> residentActivities(@PathVariable Long id) {
        return activityService.getResidentActivities(id);
    }

    @GetMapping("/vendor/{id}")
    public List<ActivityEntity> vendorActivities(@PathVariable Long id) {
        return activityService.getVendorActivities(id);
    }
}

