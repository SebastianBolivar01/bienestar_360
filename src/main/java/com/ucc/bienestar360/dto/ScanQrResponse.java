package com.ucc.bienestar360.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScanQrResponse {
    private boolean success;
    private String message;
    private String activityTitle;
    private Double hoursAwarded;
    private Double totalCompletedHours;
}
