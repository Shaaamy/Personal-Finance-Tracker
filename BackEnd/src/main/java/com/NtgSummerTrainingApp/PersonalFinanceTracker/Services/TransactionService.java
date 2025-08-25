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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
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


    // add transaction
    public TransactionDTO addTransaction(TransactionDTO dto, Long loggedInUserId) {
        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero");
        }
        User user = userRepo.findById(loggedInUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        if (dto.getDescription() != null){
            transaction.setDescription(dto.getDescription());
        }else {
            transaction.setDescription("No Description");
        }        transaction.setCurrency(dto.getCurrency());
        transaction.setDate(dto.getDate());
        transaction.setUser(user);
        transaction.setCategory(category);

        // Handle balance updates safely
        if (transaction.getType() == CategoryTypeEnum.EXPENSE) {
            if (user.getBalance().compareTo(transaction.getAmount()) < 0) {
                throw new IllegalArgumentException("Insufficient balance for this expense transaction");
            }
            user.setBalance(user.getBalance().subtract(transaction.getAmount()));
        } else if (transaction.getType() == CategoryTypeEnum.INCOME) {
            user.setBalance(user.getBalance().add(transaction.getAmount()));
        }

        userRepo.save(user);

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
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
        return TransactionMapper.toDTO(transaction);
    }

    //  Get all transactions for a user
    public List<TransactionDTO> getTransactionsByUser(long userId) {
        List<Transaction> transactions = transactionRepo.findByUserId(userId);
        return transactions.stream().map(TransactionMapper::toDTO).toList();
    }

    //  Delete a transaction
    public void deleteTransaction(Long id, long loggedInUserId) {
        Transaction transaction = transactionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
        if(!(transaction.getUser().getId() == loggedInUserId)){
            throw new AccessDeniedException("You are not authorized to delete this transaction.");
        }
        transactionRepo.deleteById(id);
    }


    public List<TransactionDTO> filterTransactions(Long loggedInUserId,
                                                   Long categoryId,
                                                   CategoryTypeEnum type,
                                                   LocalDate startDate,
                                                   LocalDate endDate
                                                   ) {

        List<Transaction> transactions = transactionRepo.findByUserId(loggedInUserId);

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
