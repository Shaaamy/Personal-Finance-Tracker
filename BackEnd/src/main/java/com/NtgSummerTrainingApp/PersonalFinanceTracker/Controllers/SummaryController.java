package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.SummaryService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.*;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("summaries")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<MonthlySummaryDto>> getMonthlySummary(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        long loggedInUserId = userPrincipal.getUser().getId();
        MonthlySummaryDto summary = summaryService.getMonthlySummary(loggedInUserId, month, year);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Monthly summary fetched successfully", summary)
        );
    }

    @GetMapping("/annually")
    public ResponseEntity<ApiResponse<AnnualSummaryDto>> getAnnualSummary(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam int year) {
        long loggedInUserId = userPrincipal.getUser().getId();
        AnnualSummaryDto summary = summaryService.getAnnualSummary(loggedInUserId, year);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Annual summary fetched successfully", summary)
        );
    }

    @GetMapping("/basicStatistics")
    public ResponseEntity<ApiResponse<BasicStatisticsDto>> getBasicStatistics(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam int year,
            @RequestParam int month) {

        long loggedInUserId = userPrincipal.getUser().getId();
        BasicStatisticsDto stats = summaryService.basicStatistics(loggedInUserId, year, month);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Basic statistics fetched successfully", stats)
        );

    }
}
