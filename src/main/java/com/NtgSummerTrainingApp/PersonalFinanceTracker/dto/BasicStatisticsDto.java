package com.NtgSummerTrainingApp.PersonalFinanceTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicStatisticsDto {
    private BigDecimal maxExpense;
    private String highestExpenseCategory;
    private BigDecimal averageMonthlySpending;
    private BigInteger percentageChangeFromLastMonth;
}
