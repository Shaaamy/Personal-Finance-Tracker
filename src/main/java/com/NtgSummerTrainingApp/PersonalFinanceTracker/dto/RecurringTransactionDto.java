package com.NtgSummerTrainingApp.PersonalFinanceTracker.dto;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryTypeEnum;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.FrequencyEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurringTransactionDto {
    private Long id;
    private BigDecimal amount;
    private CategoryTypeEnum type;
    private FrequencyEnum frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private String description;
}
