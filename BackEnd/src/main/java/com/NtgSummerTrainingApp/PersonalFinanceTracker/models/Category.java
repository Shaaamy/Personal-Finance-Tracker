package com.NtgSummerTrainingApp.PersonalFinanceTracker.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryTypeEnum type;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @ManyToMany(mappedBy = "categories")
    @JsonBackReference
    private Set<User> users;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Transaction> transactions;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Budget> budgets;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<RecurringTransaction> recurringTransactions;


    public Category(String name, CategoryTypeEnum type, Set<User> users, Set<Transaction> transactions, Set<Budget> budgets, Set<RecurringTransaction> recurringTransactions) {
        this.name = name;
        this.type = type;
        this.users = users;
        this.transactions = transactions;
        this.budgets = budgets;
        this.recurringTransactions = recurringTransactions;
    }


}
