package com.ucc.bienestar360.controller;

import com.ucc.bienestar360.dto.StudentProgressDto;
import com.ucc.bienestar360.model.Attendance;
import com.ucc.bienestar360.service.AttendanceService;
import com.ucc.bienestar360.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentController {

    private final UserService userService;
    private final AttendanceService attendanceService;

    @GetMapping("/{studentId}/progress")
    public ResponseEntity<?> getProgress(@PathVariable Long studentId) {
        try {
            StudentProgressDto progress = userService.getStudentProgress(studentId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{studentId}/history")
    public ResponseEntity<List<Attendance>> getHistory(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendances(studentId));
    }
}
