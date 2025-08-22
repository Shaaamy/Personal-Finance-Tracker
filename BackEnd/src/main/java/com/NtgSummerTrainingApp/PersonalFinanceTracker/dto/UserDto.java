package com.NtgSummerTrainingApp.PersonalFinanceTracker.dto;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.RoleEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;
    @NotBlank(message = "Username is required")
    private String username;
    private BigDecimal balance = BigDecimal.valueOf(0);
    @NotBlank(message = "Email is required")
    @Email
    private String email;
    private String fullName;
    @NotBlank(message = "Password is required")
    private String password;
    private RoleEnum role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
