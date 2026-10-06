package com.ucc.bienestar360.dto;

import com.ucc.bienestar360.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private boolean success;
    private String message;
    private User user;
    private String token;
}
