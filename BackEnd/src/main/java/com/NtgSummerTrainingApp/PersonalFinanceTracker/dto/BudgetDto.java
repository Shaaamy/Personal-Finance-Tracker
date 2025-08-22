package com.NtgSummerTrainingApp.PersonalFinanceTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetDto {
    private Long id;
    private Integer month;
    private Integer year;
    private BigDecimal amount;
    private Long userId;
    private Long categoryId;
    private String categoryName;
}

