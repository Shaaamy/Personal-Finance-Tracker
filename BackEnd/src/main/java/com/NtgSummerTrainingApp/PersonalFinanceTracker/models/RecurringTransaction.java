package com.NtgSummerTrainingApp.PersonalFinanceTracker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recurring_transactions")
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecurringTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    @Column(nullable = false)
    private BigDecimal amount;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryTypeEnum type;

    @NotNull(message = "Frequency is required")
    @Enumerated(EnumType.STRING)
    private FrequencyEnum frequency;

    @NotNull(message = "Active status is required")
    private Boolean active = true;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    private LocalDate nextDueDate;

    public boolean isDueToday() {
        return nextDueDate != null && LocalDate.now().isEqual(this.nextDueDate);
    }


    public void updateNextDueDate() {
        if (nextDueDate == null || frequency == null) return;

        switch (this.frequency) {
            case DAILY -> this.nextDueDate = this.nextDueDate.plusDays(1);
            case WEEKLY -> this.nextDueDate = this.nextDueDate.plusWeeks(1);
            case MONTHLY -> this.nextDueDate = this.nextDueDate.plusMonths(1);
            case HALF_ANNUAL -> this.nextDueDate = this.nextDueDate.plusMonths(6);
            case ANNUAL -> this.nextDueDate = this.nextDueDate.plusYears(1);
        }
    }


}
