package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.JwtService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.RecurringTransactionService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.ApiResponse;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.RecurringTransactionDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recurring-transactions")
@RequiredArgsConstructor
public class RecurringTransactionController {

    private final RecurringTransactionService recurringTransactionService;
    private final JwtService jwtService;

    @PostMapping()
    public ResponseEntity<ApiResponse<RecurringTransactionDto>> createRecurringTransaction(
            @Valid @RequestBody RecurringTransactionDto dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        long loggedInUserId = userPrincipal.getUser().getId();// Long type
        // Override any userId passed in the request to prevent privilege escalation
        dto.setUserId(loggedInUserId);
        RecurringTransactionDto createdTransaction = recurringTransactionService.createRecurringTransaction(dto,loggedInUserId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Recurring transaction created successfully", createdTransaction));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<RecurringTransactionDto>>> getAllByUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        long loggedInUserId = userPrincipal.getUser().getId();// Long type
        List<RecurringTransactionDto> list = recurringTransactionService.getAllByUser(loggedInUserId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Recurring transactions fetched successfully", list));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable Long id,
            @Valid @RequestBody RecurringTransactionDto dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long loggedInUserId = userPrincipal.getUser().getId();
        RecurringTransactionDto updated = recurringTransactionService.update(id, dto, loggedInUserId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Recurring transaction updated successfully", updated));
    }


    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<ApiResponse<RecurringTransactionDto>> deactivateRecurringTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long loggedInUserId = userPrincipal.getUser().getId();
        RecurringTransactionDto updated = recurringTransactionService.update(id,
                RecurringTransactionDto.builder().isActive(false).build(),
                loggedInUserId);

        return ResponseEntity.ok(new ApiResponse<>(true,
                "Recurring transaction deactivated successfully", updated));
    }
}
