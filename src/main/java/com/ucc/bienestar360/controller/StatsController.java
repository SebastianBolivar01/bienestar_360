package com.ucc.bienestar360.controller;

import com.ucc.bienestar360.dto.StatsDto;
import com.ucc.bienestar360.model.Role;
import com.ucc.bienestar360.model.User;
import com.ucc.bienestar360.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StatsController {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final AttendanceRepository attendanceRepository;
    private final QrSessionRepository qrSessionRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<StatsDto> getDashboardStats() {
        long totalStudents = userRepository.findByRole(Role.ESTUDIANTE).size();
        long totalActivities = activityRepository.count();
        long totalAttendances = attendanceRepository.count();

        double totalHoursGranted = attendanceRepository.findAll()
                .stream()
                .mapToDouble(a -> a.getHoursAwarded() != null ? a.getHoursAwarded() : 0.0)
                .sum();

        long activeQr = qrSessionRepository.findAll()
                .stream()
                .filter(s -> s.isActive())
                .count();

        StatsDto stats = StatsDto.builder()
                .totalStudents(totalStudents)
                .totalActivities(totalActivities)
                .totalHoursGranted(Math.round(totalHoursGranted * 10.0) / 10.0)
                .totalAttendances(totalAttendances)
                .activeQrSessions(activeQr)
                .build();

        return ResponseEntity.ok(stats);
    }
}
