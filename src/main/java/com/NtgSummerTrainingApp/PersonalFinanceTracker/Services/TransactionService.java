package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.PaginationMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.TransactionMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationRequest;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.TransactionDTO;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.helper.PaginationHelper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.*;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.CategoryRepository;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.TransactionRepo;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        return TransactionMapper.toDTO(saved);
    }

    // Get all transactions
    public PaginationDto<TransactionDTO> getAllTransactions(PaginationRequest paginationReq){
        Page<Transaction> transactions = transactionRepo.findAll(PaginationHelper.getPageable(paginationReq));
        if(transactions.isEmpty()){
            throw new EntityNotFoundException("No Transactions Found");
        }
        Page<TransactionDTO> transactionsDtosPage = transactions.map(TransactionMapper::toDTO);
        return PaginationMapper.toPaginatedDto(transactionsDtosPage);
    }

    //  Get transaction by ID
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return TransactionMapper.toDTO(transaction);
    }

    //  Get all transactions for a user
    public List<TransactionDTO> getTransactionsByUser(Long userId) {
        List<Transaction> transactions = transactionRepo.findByUserId(userId);
        return transactions.stream().map(TransactionMapper::toDTO).toList();
    }

    //  Delete a transaction
    public void deleteTransaction(Long id) {
        if (!transactionRepo.existsById(id)) {
            throw new RuntimeException("Transaction not found");
        }
        transactionRepo.deleteById(id);
    }


    public List<TransactionDTO> filterTransactions(Long userId,
                                                   Long categoryId,
                                                   CategoryTypeEnum type,
                                                   LocalDate startDate,
                                                   LocalDate endDate
                                                   ) {

        List<Transaction> transactions = transactionRepo.findByUserId(userId);

        if (startDate != null) {
            transactions = transactions.stream()
                    .filter(t -> !t.getDate().isBefore(startDate))
                    .toList();
        }

        if (endDate != null) {
            transactions = transactions.stream()
                    .filter(t -> !t.getDate().isAfter(endDate))
                    .toList();
        }

        if (type != null) {
            transactions = transactions.stream()
                    .filter(t -> t.getType().name().equalsIgnoreCase(String.valueOf(type)))
                    .toList();
        }

        if (categoryId != null) {
            transactions = transactions.stream()
                    .filter(t -> t.getCategory() != null && t.getCategory().getId().equals(categoryId))
                    .toList();
        }

        return transactions.stream()
                .map(TransactionMapper::toDTO)
                .toList();
    }


    public Transaction createFromRecurring(RecurringTransaction rt) {
        Transaction tx = new Transaction();
        tx.setAmount(rt.getAmount());
        tx.setType(rt.getType());
        tx.setCategory(rt.getCategory());
        tx.setDate(LocalDate.now());
        tx.setUser(rt.getUser());
        tx.setDescription(rt.getDescription());
        return transactionRepo.save(tx);
    }
}
