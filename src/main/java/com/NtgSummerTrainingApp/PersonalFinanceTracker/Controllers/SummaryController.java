package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.SummaryService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.AnnualSummaryDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.BasicStatisticsDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.MonthlySummaryDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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
    @GetMapping("/annually")
    public ResponseEntity<AnnualSummaryDto> getAnnualSummary(
            @RequestParam Long userId,
            @RequestParam int year) {

        AnnualSummaryDto summary = summaryService.getAnnualSummary(userId, year);
        return ResponseEntity.ok(summary);
    }
    @GetMapping("/basicStatistics")
    public ResponseEntity<BasicStatisticsDto> getBasicStatistics(@RequestParam long id , @RequestParam int year , @RequestParam int month){
        return new ResponseEntity<>(summaryService.basicStatistics(id,year,month ), HttpStatus.OK);
    }
}
