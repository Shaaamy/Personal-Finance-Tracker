package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.RecurringTransactionMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.RecurringTransactionDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Category;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.RecurringTransaction;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.User;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.CategoryRepository;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.RecurringTransactionRepo;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
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

    public RecurringTransactionDto createRecurringTransaction(RecurringTransactionDto dto, Long loggedInUserId) {
        // Get logged-in user
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Get category
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        // Map DTO to entity
        RecurringTransaction transaction = RecurringTransactionMapper.toEntity(dto);
        if (dto.getDescription() != null){
            transaction.setDescription(dto.getDescription());
        }else {
            transaction.setDescription("No Description");
        }
        // Assign user and category
        transaction.setUser(user);
        transaction.setCategory(category);
        // Initialize nextDueDate if active
        if (transaction.getActive() == null) {
            transaction.setActive(true); // default active
        }
        if (transaction.getActive() && transaction.getNextDueDate() == null) {
            transaction.setNextDueDate(transaction.getStartDate());
        }

        RecurringTransaction saved = recurringTransactionRepo.save(transaction);

        return RecurringTransactionMapper.toDto(saved);
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


    public List<RecurringTransactionDto> getAllByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User Not Found"));
        List<RecurringTransaction> list = recurringTransactionRepository.findByUserId(userId);
        return list.stream()
                .map(RecurringTransactionMapper::toDto)
                .toList();
    }

//    public RecurringTransactionDto update(Long id, RecurringTransactionDto dto) {
//        RecurringTransaction existing = recurringTransactionRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Recurring transaction not found"));
//
//        existing.setAmount(dto.getAmount());
//        existing.setType(dto.getType());
//        existing.setFrequency(dto.getFrequency());
//        existing.setStartDate(dto.getStartDate());
//        existing.setEndDate(dto.getEndDate());
//        existing.setDescription(dto.getDescription());
//
//        if (dto.getCategoryId() != null) {
//            Category category = categoryRepository.findById(dto.getCategoryId())
//                    .orElseThrow(() -> new RuntimeException("Category not found"));
//            existing.setCategory(category);
//        }
//
//        RecurringTransaction updated = recurringTransactionRepository.save(existing);
//        return RecurringTransactionMapper.toDto(updated);
//    }
    public RecurringTransactionDto update(Long transactionId, RecurringTransactionDto request, Long loggedInUserId) {
        RecurringTransaction transaction = recurringTransactionRepo.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Recurring transaction not found"));

        // Ownership check
        if (!(transaction.getUser().getId()==(loggedInUserId))) {
            throw new AccessDeniedException("You are not authorized to update this transaction.");
        }

        // Update fields from request if present
        if (request.getAmount() != null) transaction.setAmount(request.getAmount());
        if(request.getType()!=null) transaction.setType(request.getType());
        if (request.getDescription() != null){
            transaction.setDescription(request.getDescription());
        }else {
            transaction.setDescription("No Description");
        }
        if (request.getFrequency() != null) transaction.setFrequency(request.getFrequency());
        if (request.getCategoryId() != null) {
            transaction.setCategory(categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found")));
        }
        if (request.getStartDate() != null) transaction.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) transaction.setEndDate(request.getEndDate());
        if (request.getIsActive() != null) {
            transaction.setActive(request.getIsActive());

            // Initialize nextDueDate if activating
            if (request.getIsActive() && transaction.getNextDueDate() == null) {
                transaction.setNextDueDate(transaction.getStartDate());
            }
        }
        // Preserve nextDueDate unless user explicitly sends it
        if (request.getNextDueDate() != null) {
            transaction.setNextDueDate(request.getNextDueDate());
        }
        recurringTransactionRepo.save(transaction);

        return RecurringTransactionMapper.toDto(transaction);
    }

    public void delete(Long id) {
        if (!recurringTransactionRepository.existsById(id)) {
            throw new RuntimeException("Recurring transaction not found");
        }
        recurringTransactionRepository.deleteById(id);
    }
    // Deactivate recurring transaction (soft delete)
    public RecurringTransactionDto deactivate(Long transactionId, Long loggedInUserId) {
        RecurringTransaction transaction = recurringTransactionRepo.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Recurring transaction not found"));

        // Ownership check
        if (!(transaction.getUser().getId()==(loggedInUserId))) {
            throw new AccessDeniedException("You are not authorized to deactivate this transaction.");
        }

        transaction.setActive(false);
        recurringTransactionRepo.save(transaction);
        return RecurringTransactionMapper.toDto(transaction);
    }


}
