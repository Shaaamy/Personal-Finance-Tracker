package com.NtgSummerTrainingApp.PersonalFinanceTracker.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryType;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType type;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "categories")
    @JsonBackReference
    private Set<User> users;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Transaction> transactions;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Budget> budgets;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<RecurringTransaction> recurringTransactions;
}
