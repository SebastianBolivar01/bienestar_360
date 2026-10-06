package com.ucc.bienestar360.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsDto {
    private long totalStudents;
    private long totalActivities;
    private double totalHoursGranted;
    private long totalAttendances;
    private long activeQrSessions;
}
