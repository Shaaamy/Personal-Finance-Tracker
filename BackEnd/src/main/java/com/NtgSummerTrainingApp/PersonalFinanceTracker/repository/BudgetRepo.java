package com.NtgSummerTrainingApp.PersonalFinanceTracker.repository;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepo extends JpaRepository<Budget, Long> {
    List<Budget> findByUserIdAndMonthAndYear(Long userId, Integer month, Integer year);
    Optional<Budget> findByUserIdAndCategoryIdAndMonthAndYear(Long userId, Long categoryId, Integer month, Integer year);
    List<Budget> findByUserId(Long userId);
}
