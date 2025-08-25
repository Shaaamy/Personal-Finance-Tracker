package com.NtgSummerTrainingApp.PersonalFinanceTracker.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponseDto {
    private long id;
    private String username;
    private String fullName;
    private String email;
    private String role;
    private String accessToken;
    private String refreshToken;



}