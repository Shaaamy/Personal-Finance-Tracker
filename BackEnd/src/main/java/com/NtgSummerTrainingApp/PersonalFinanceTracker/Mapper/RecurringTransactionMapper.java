package com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper;


import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.RecurringTransactionDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.RecurringTransaction;

public class RecurringTransactionMapper {

    public static RecurringTransactionDto toDto(RecurringTransaction transaction) {
        if (transaction == null) return null;

        return RecurringTransactionDto.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .frequency(transaction.getFrequency())
                .startDate(transaction.getStartDate())
                .endDate(transaction.getEndDate())
                .description(transaction.getDescription())
                .userId(transaction.getUser() != null ? transaction.getUser().getId() : null)
                .categoryId(transaction.getCategory() != null ? transaction.getCategory().getId() : null)
                .categoryName(transaction.getCategory() != null ? transaction.getCategory().getName() : null)
                .build();
    }

    public static RecurringTransaction toEntity(RecurringTransactionDto dto) {
        if (dto == null) return null;

        RecurringTransaction transaction = new RecurringTransaction();
        transaction.setId(dto.getId());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setFrequency(dto.getFrequency());
        transaction.setStartDate(dto.getStartDate());
        transaction.setEndDate(dto.getEndDate());
        transaction.setDescription(dto.getDescription());
        return transaction;
    }
}
