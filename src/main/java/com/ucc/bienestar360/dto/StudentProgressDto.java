package com.ucc.bienestar360.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProgressDto {
    private Long studentId;
    private String fullName;
    private String institutionalCode;
    private String academicProgram;
    private Double completedHours;
    private Double requiredHours;
    private Double pendingHours;
    private Double progressPercentage;
    private Integer totalEnrollments;
    private Integer totalAttendances;
}
