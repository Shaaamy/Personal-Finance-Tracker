package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.SummaryService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.MonthlySummaryDto;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<MonthlySummaryDto> getMonthlySummary(
            @RequestParam Long userId,
            @RequestParam Integer month,
            @RequestParam Integer year) {

        MonthlySummaryDto summary = summaryService.getMonthlySummary(userId, month, year);
        return ResponseEntity.ok(summary);
    }
}
