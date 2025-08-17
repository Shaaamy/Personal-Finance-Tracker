package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.BudgetDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Budget;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Category;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Transaction;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.User;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.BudgetRepo;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.CategoryRepository;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.TransactionRepo;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepo budgetRepository;
    private final UserRepo userRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepo transactionRepository;


    public BudgetDto setBudget(BudgetDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Budget budget = budgetRepository.findByUserIdAndCategoryIdAndMonthAndYear(
                dto.getUserId(), dto.getCategoryId(), dto.getMonth(), dto.getYear()
        ).orElse(new Budget());

        budget.setUser(user);
        budget.setCategory(category);
        budget.setMonth(dto.getMonth());
        budget.setYear(dto.getYear());
        budget.setAmount(dto.getAmount());

        Budget saved = budgetRepository.save(budget);

        return toDTO(saved);
    }


    public BigDecimal getSpendingForBudget(Long userId, Long categoryId, Integer month, Integer year) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId).stream()
                .filter(t -> t.getCategory().getId().equals(categoryId)
                        && t.getDate().getMonthValue() == month
                        && t.getDate().getYear() == year
                        && t.getType().name().equals("EXPENSE"))
                .toList();

        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private BudgetDto toDTO(Budget budget) {
        return new BudgetDto(
                budget.getId(),
                budget.getMonth(),
                budget.getYear(),
                budget.getAmount(),
                budget.getUser().getId(),
                budget.getCategory().getId(),
                budget.getCategory().getName()
        );
    }
}
