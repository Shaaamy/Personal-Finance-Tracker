package com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.TransactionDTO;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Transaction;

public class TransactionMapper {
    public static TransactionDTO toDTO(Transaction transaction){
        return TransactionDTO.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .description(transaction.getDescription())
                .currency(transaction.getCurrency())
                .date(transaction.getDate())
                .createdAt(transaction.getCreatedAt())
                .userId(transaction.getUser().getId())
                .categoryId(transaction.getCategory().getId())
                .categoryName(transaction.getCategory().getName())
                .build();
    }
}
