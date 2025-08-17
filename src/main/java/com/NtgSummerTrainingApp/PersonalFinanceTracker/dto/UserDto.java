package com.NtgSummerTrainingApp.PersonalFinanceTracker.dto;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.RoleEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String password;
    private RoleEnum role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
