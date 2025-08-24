package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.BudgetService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.ApiResponse;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.BudgetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;


    @PostMapping
    public ResponseEntity<ApiResponse<BudgetDto>> setBudget(@RequestBody BudgetDto dto) {
        BudgetDto saved = budgetService.setBudget(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Budget created successfully", saved));
    }


    @GetMapping("/check")
    public ResponseEntity<ApiResponse<BigDecimal>> checkBudget(
            @RequestParam Long userId,
            @RequestParam Long categoryId,
            @RequestParam Integer month,
            @RequestParam Integer year) {

        BigDecimal spending = budgetService.getSpendingForBudget(userId, categoryId, month, year);
        return ResponseEntity.ok(new ApiResponse<>(true, "Total spending for this budget fetched successfully", spending));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BudgetDto>>> getAllBudgetsForUser(@PathVariable Long userId) {
        List<BudgetDto> budgets = budgetService.getAllBudgetsForUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Budgets fetched successfully", budgets));
    }

    @DeleteMapping("/user/{userId}/{budgetId}")
    public ResponseEntity<ApiResponse<String>> deleteBudgetForUser(
            @PathVariable Long userId,
            @PathVariable Long budgetId) {
        try {
            budgetService.deleteBudgetForUser(userId, budgetId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Budget deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}