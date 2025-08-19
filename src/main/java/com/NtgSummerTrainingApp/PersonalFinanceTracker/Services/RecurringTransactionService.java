package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.RecurringTransactionDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Category;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.RecurringTransaction;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.User;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.CategoryRepository;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.RecurringTransactionRepo;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.RecurringTransactionMapper.toDto;
import static com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.RecurringTransactionMapper.toEntity;

@Service
@RequiredArgsConstructor
public class RecurringTransactionService {

    private final RecurringTransactionRepo recurringTransactionRepository;
    private final UserRepo userRepository;
    private final CategoryRepository categoryRepository;
    private final RecurringTransactionRepo recurringTransactionRepo;
    private final TransactionService transactionService;

    public RecurringTransactionDto createRecurringTransaction(RecurringTransactionDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        RecurringTransaction transaction = toEntity(dto);
        transaction.setUser(user);
        transaction.setCategory(category);

        RecurringTransaction saved = recurringTransactionRepository.save(transaction);
        return toDto(saved);
    }


    @Scheduled(cron = "0 0 0 * * ?")     // runs every day at midnight
    public void processRecurringTransactions() {
        List<RecurringTransaction> recurringList = recurringTransactionRepo.findByActiveTrue();
        for (RecurringTransaction rt : recurringList) {
            if (rt.isDueToday()) {
                transactionService.createFromRecurring(rt);
                rt.updateNextDueDate();
                recurringTransactionRepo.save(rt);
            }
        }
    }

}
