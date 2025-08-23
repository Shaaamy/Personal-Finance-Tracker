package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.RecurringTransactionService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.ApiResponse;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.RecurringTransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recurring-transactions")
@RequiredArgsConstructor
public class RecurringTransactionController {

    private final RecurringTransactionService recurringTransactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<RecurringTransactionDto>> createRecurringTransaction(
            @RequestBody RecurringTransactionDto dto) {

        RecurringTransactionDto saved = recurringTransactionService.createRecurringTransaction(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Recurring transaction created successfully", saved));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<RecurringTransactionDto>>> getAllByUser(@PathVariable Long userId) {
        List<RecurringTransactionDto> list = recurringTransactionService.getAllByUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Recurring transactions fetched successfully", list));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable Long id,
            @RequestBody RecurringTransactionDto dto) {
        try {
            RecurringTransactionDto updated = recurringTransactionService.update(id, dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Recurring transaction updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        try {
            recurringTransactionService.delete(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Recurring transaction deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
