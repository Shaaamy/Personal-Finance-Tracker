package com.NtgSummerTrainingApp.PersonalFinanceTracker.dto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryTypeEnum;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CurrencyType;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private CategoryTypeEnum type;
    private String description;
    private CurrencyType currency;
    private LocalDate date;
    private LocalDateTime createdAt;

    private Long userId;
    private Long categoryId;
    private String categoryName;
}
