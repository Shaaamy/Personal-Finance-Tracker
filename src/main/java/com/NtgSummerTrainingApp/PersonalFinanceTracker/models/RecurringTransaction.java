package com.NtgSummerTrainingApp.PersonalFinanceTracker.models;

import jakarta.persistence.*;
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

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryTypeEnum type;

    @Enumerated(EnumType.STRING)
    private FrequencyEnum frequency;

    Boolean active = true;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private LocalDate nextDueDate;

    public boolean isDueToday() {
        return LocalDate.now().isEqual(this.nextDueDate);
    }


    public void updateNextDueDate() {
        switch (this.frequency) {
            case DAILY -> this.nextDueDate = this.nextDueDate.plusDays(1);
            case WEEKLY -> this.nextDueDate = this.nextDueDate.plusWeeks(1);
            case MONTHLY -> this.nextDueDate = this.nextDueDate.plusMonths(1);
            case HALF_ANNUAL -> this.nextDueDate = this.nextDueDate.plusMonths(6);
            case ANNUAL -> this.nextDueDate = this.nextDueDate.plusYears(1);
        }
    }


}
