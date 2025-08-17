package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.BudgetService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.BudgetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;


    @PostMapping
    public ResponseEntity<BudgetDto> setBudget(@RequestBody BudgetDto dto) {
        BudgetDto saved = budgetService.setBudget(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @GetMapping("/check")
    public ResponseEntity<String> checkBudget(
            @RequestParam Long userId,
            @RequestParam Long categoryId,
            @RequestParam Integer month,
            @RequestParam Integer year) {

        BigDecimal spending = budgetService.getSpendingForBudget(userId, categoryId, month, year);

        return ResponseEntity.ok("Total spending for this budget: " + spending);
    }
}
