package com.ucc.bienestar360.controller;

import com.ucc.bienestar360.dto.CreateActivityRequest;
import com.ucc.bienestar360.model.Activity;
import com.ucc.bienestar360.model.ActivityStatus;
import com.ucc.bienestar360.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<List<Activity>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getActivityById(id));
    }

    @PostMapping
    public ResponseEntity<?> createActivity(@RequestBody CreateActivityRequest request) {
        try {
            Activity created = activityService.createActivity(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Activity> updateStatus(@PathVariable Long id, @RequestParam ActivityStatus status) {
        return ResponseEntity.ok(activityService.updateStatus(id, status));
    }
}
