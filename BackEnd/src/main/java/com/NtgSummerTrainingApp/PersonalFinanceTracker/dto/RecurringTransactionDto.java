package com.NtgSummerTrainingApp.PersonalFinanceTracker.dto;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryTypeEnum;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.FrequencyEnum;
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
public class RecurringTransactionDto {
    private Long id;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Transaction type is required")
    private CategoryTypeEnum type;

    @NotNull(message = "Frequency is required")
    private FrequencyEnum frequency;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private Long userId; // will be overridden from token, no validation needed

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private String categoryName; // populated by service, no validation

    @Size(max = 500, message = "Description can be at most 500 characters")
    private String description;

    private Boolean isActive; // optional, defaults handled in mapper/service

    private LocalDate nextDueDate; // optional, handled in mapper/service
}
