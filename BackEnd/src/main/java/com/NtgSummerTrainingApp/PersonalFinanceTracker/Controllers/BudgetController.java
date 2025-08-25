package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.BudgetService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.ApiResponse;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.BudgetDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.User;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("budgets")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class BudgetController {

    private final BudgetService budgetService;


    @PostMapping
    public ResponseEntity<ApiResponse<BudgetDto>> setBudget(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody BudgetDto dto) {
        long loggedInUserId = userPrincipal.getUser().getId();
        dto.setUserId(loggedInUserId);
        BudgetDto saved = budgetService.setBudget(loggedInUserId,dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Budget created successfully", saved));
    }


    @GetMapping("/check")
    public ResponseEntity<ApiResponse<BigDecimal>> checkBudget(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam Long categoryId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        long loggedInUserId = userPrincipal.getUser().getId();
        BigDecimal spending = budgetService.getSpendingForBudget(loggedInUserId, categoryId, month, year);
        return ResponseEntity.ok(new ApiResponse<>(true, "Total spending for this budget fetched successfully", spending));
    }


    @GetMapping()
    public ResponseEntity<ApiResponse<List<BudgetDto>>> getAllBudgetsForUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        long loggedInUserId = userPrincipal.getUser().getId();
        List<BudgetDto> budgets = budgetService.getAllBudgetsForUser(loggedInUserId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Budgets fetched successfully", budgets));
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<ApiResponse<String>> deleteForUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long budgetId) {
        long loggedInUserId = userPrincipal.getUser().getId();
        budgetService.deleteBudgetForUser(loggedInUserId, budgetId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Budget deleted successfully", null));
    }
}