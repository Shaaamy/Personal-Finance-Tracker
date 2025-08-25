package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.TransactionService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.*;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryTypeEnum;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("transaction")
@RequiredArgsConstructor
@Validated
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TransactionDTO>> addTransaction(@RequestBody @Valid TransactionDTO dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long LoggedInUserId = userPrincipal.getUser().getId();
        dto.setUserId(LoggedInUserId);
        TransactionDTO saved = transactionService.addTransaction(dto, LoggedInUserId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Transaction created successfully", saved));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDTO>> getTransaction(@PathVariable Long id) {
        TransactionDTO dto = transactionService.getTransactionById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Transaction fetched successfully", dto));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<ApiResponse<PaginationDto<TransactionDTO>>> getAllTransactions(@ModelAttribute PaginationRequest paginationReq) {
        PaginationDto<TransactionDTO> page = transactionService.getAllTransactions(paginationReq);
        return ResponseEntity.ok(new ApiResponse<>(true, "Transactions fetched successfully", page));
    }

    @GetMapping("/for-user")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsForUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        long loggedInUserId = userPrincipal.getUser().getId();
        List<TransactionDTO> transactions = transactionService.getTransactionsByUser(loggedInUserId);
        return ResponseEntity.ok(new ApiResponse<>(true, "User transactions fetched successfully", transactions));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long loggedInUserId = userPrincipal.getUser().getId();
        transactionService.deleteTransaction(id, loggedInUserId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Transaction deleted successfully", null));
    }


    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> filterTransactions(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) CategoryTypeEnum type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        long loggedInUserId = userPrincipal.getUser().getId();
        List<TransactionDTO> transactions =
                transactionService.filterTransactions(loggedInUserId, categoryId, type, startDate, endDate);
        return ResponseEntity.ok(new ApiResponse<>(true, "Transactions filtered successfully", transactions));

    }
}
