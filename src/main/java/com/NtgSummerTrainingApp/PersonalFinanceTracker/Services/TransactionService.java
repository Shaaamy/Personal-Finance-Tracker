package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.TransactionDTO;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Category;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Transaction;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.User;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.CategoryRepository;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.TransactionRepo;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public TransactionDTO addTransaction(TransactionDTO dto) {
        Transaction transaction = new Transaction();

        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setDescription(dto.getDescription());
        transaction.setCurrency(dto.getCurrency());
        transaction.setDate(dto.getDate());

        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        transaction.setUser(user);

        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        transaction.setCategory(category);

        Transaction saved = transactionRepo.save(transaction);
        return toDTO(saved);
    }

    //  Get transaction by ID
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return toDTO(transaction);
    }

    //  Get all transactions for a user
    public List<TransactionDTO> getTransactionsByUser(Long userId) {
        List<Transaction> transactions = transactionRepo.findByUserId(userId);
        return transactions.stream().map(this::toDTO).toList();
    }

    //  Delete a transaction
    public void deleteTransaction(Long id) {
        if (!transactionRepo.existsById(id)) {
            throw new RuntimeException("Transaction not found");
        }
        transactionRepo.deleteById(id);
    }

    private TransactionDTO toDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDescription(),
                transaction.getCurrency(),
                transaction.getDate(),
                transaction.getCreatedAt(),
                transaction.getUser().getId(),
                transaction.getCategory().getId(),
                transaction.getCategory().getName()
        );
    }

}
