package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.RecurringTransactionService;
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
    public ResponseEntity<RecurringTransactionDto> createRecurringTransaction(
            @RequestBody RecurringTransactionDto dto) {

        RecurringTransactionDto saved = recurringTransactionService.createRecurringTransaction(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecurringTransactionDto>> getAllByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(recurringTransactionService.getAllByUser(userId));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody RecurringTransactionDto dto) {
        try {
            return ResponseEntity.ok(recurringTransactionService.update(id, dto));
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            recurringTransactionService.delete(id);
            return new ResponseEntity<>("Recurring Transaction deleted successfully", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
