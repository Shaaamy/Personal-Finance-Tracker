package com.NtgSummerTrainingApp.PersonalFinanceTracker.dto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryTypeEnum;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CurrencyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "type is required")
    private CategoryTypeEnum type;

    private String description;

    @NotNull(message = "currency is required")
    private CurrencyType currency;

    private LocalDate date;
    private LocalDateTime createdAt;

    private Long userId;
    private Long categoryId;
    private String categoryName;
}
