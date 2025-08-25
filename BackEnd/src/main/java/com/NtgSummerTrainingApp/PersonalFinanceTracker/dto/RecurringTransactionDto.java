package com.NtgSummerTrainingApp.PersonalFinanceTracker.dto;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryTypeEnum;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.FrequencyEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecurringTransactionDto {
    private Long id;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Transaction type is required")
    private CategoryTypeEnum type;

    @NotNull(message = "Frequency is required")
    private FrequencyEnum frequency;

    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @NotNull(message = "Category ID is required")

    @NotNull(message = "User ID is required")
    private Long userId;
    private Long categoryId;

    private String categoryName; // populated by service, no validation

    @Size(max = 500, message = "Description can be at most 500 characters")
    private String description;

    private Boolean isActive; // optional, defaults handled in mapper/service

    private LocalDate nextDueDate; // optional, handled in mapper/service
}
