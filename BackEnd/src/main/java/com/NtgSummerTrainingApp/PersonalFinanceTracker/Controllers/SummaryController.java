package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.SummaryService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("summary")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<MonthlySummaryDto>> getMonthlySummary(
            @RequestParam Long userId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        try {
            MonthlySummaryDto summary = summaryService.getMonthlySummary(userId, month, year);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Monthly summary fetched successfully", summary)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/annually")
    public ResponseEntity<ApiResponse<AnnualSummaryDto>> getAnnualSummary(
            @RequestParam Long userId,
            @RequestParam int year) {
        try {
            AnnualSummaryDto summary = summaryService.getAnnualSummary(userId, year);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Annual summary fetched successfully", summary)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/basicStatistics")
    public ResponseEntity<ApiResponse<BasicStatisticsDto>> getBasicStatistics(
            @RequestParam long id,
            @RequestParam int year,
            @RequestParam int month) {
        try {
            BasicStatisticsDto stats = summaryService.basicStatistics(id, year, month);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Basic statistics fetched successfully", stats)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
