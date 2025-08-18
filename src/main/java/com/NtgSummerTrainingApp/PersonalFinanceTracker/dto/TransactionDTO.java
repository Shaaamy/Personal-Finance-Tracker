package com.NtgSummerTrainingApp.PersonalFinanceTracker.dto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryTypeEnum;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CurrencyType;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "amount is required")
    private BigDecimal amount;

    @NotBlank(message = "type is required")
    private CategoryTypeEnum type;
    private String description;

    @NotBlank(message = "currency is required")
    private CurrencyType currency;
    private LocalDate date;
    private LocalDateTime createdAt;

    private Long userId;
    private Long categoryId;
    private String categoryName;
}
