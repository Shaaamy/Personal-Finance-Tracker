package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.BudgetMapper;
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

        return BudgetMapper.toDTO(saved);
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

    public List<BudgetDto> getAllBudgetsForUser(Long userId) {
        return budgetRepository.findByUserId(userId)
                .stream()
                .map(BudgetMapper::toDTO)
                .toList();
    }


    public void deleteBudgetForUser(Long userId, Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (budget.getUser().getId() != (userId)) {
            throw new RuntimeException("This budget does not belong to the user");
        }

        budgetRepository.delete(budget);
    }
}
