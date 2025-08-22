package com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.BudgetDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Budget;

public class BudgetMapper {
    public static BudgetDto toDTO(Budget budget) {
        return BudgetDto.builder()
                .id(budget.getId())
                .year(budget.getYear())
                .amount(budget.getAmount())
                .userId(budget.getUser().getId())
                .categoryId(budget.getCategory().getId())
                .categoryName(budget.getCategory().getName())
                .build();
    }
}
