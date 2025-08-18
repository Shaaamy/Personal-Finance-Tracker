package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.RecurringTransactionService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.RecurringTransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recurring-transactions")
@RequiredArgsConstructor
public class RecurringTransactionController {

    private final RecurringTransactionService recurringTransactionService;

    @PostMapping
    public ResponseEntity<RecurringTransactionDto> createRecurringTransaction(
            @RequestBody RecurringTransactionDto dto) {

        RecurringTransactionDto saved = recurringTransactionService.createRecurringTransaction(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
