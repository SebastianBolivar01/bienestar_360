package com.ucc.bienestar360.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrGenerateResponse {
    private Long qrSessionId;
    private Long activityId;
    private String activityTitle;
    private String token;
    private LocalDateTime expiresAt;
    private String qrBase64Image;
}
