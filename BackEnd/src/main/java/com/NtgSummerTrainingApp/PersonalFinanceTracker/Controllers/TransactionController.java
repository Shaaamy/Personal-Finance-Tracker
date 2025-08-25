package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.TransactionService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.*;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryTypeEnum;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TransactionDTO>> addTransaction(@RequestBody TransactionDTO dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            Long LoggedInUserId = userPrincipal.getUser().getId();
            dto.setUserId(LoggedInUserId);
            TransactionDTO saved = transactionService.addTransaction(dto, LoggedInUserId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Transaction created successfully", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDTO>> getTransaction(@PathVariable Long id) {
        try {
            TransactionDTO dto = transactionService.getTransactionById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transaction fetched successfully", dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<ApiResponse<PaginationDto<TransactionDTO>>> getAllTransactions(@ModelAttribute PaginationRequest paginationReq) {
        try {
            PaginationDto<TransactionDTO> page = transactionService.getAllTransactions(paginationReq);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transactions fetched successfully", page));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsForUser(@PathVariable Long userId) {
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionsByUser(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "User transactions fetched successfully", transactions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transaction deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> filterTransactions(
            @RequestParam Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) CategoryTypeEnum type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<TransactionDTO> transactions =
                    transactionService.filterTransactions(userId, categoryId, type, startDate, endDate);

            return ResponseEntity.ok(new ApiResponse<>(true, "Transactions filtered successfully", transactions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
