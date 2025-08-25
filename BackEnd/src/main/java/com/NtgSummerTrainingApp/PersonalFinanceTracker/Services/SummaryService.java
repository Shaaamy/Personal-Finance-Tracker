package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.BasicStatisticsDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.MonthlySummaryDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.AnnualSummaryDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.*;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.RecurringTransactionRepo;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.TransactionRepo;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final TransactionRepo transactionRepo;
    private final RecurringTransactionRepo recurringTransactionRepo;
    public MonthlySummaryDto getMonthlySummary(Long loggedInUserId, Integer month, Integer year) {
        List<Transaction> transactions = transactionRepo.findByUserId(loggedInUserId);

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getDate() != null
                        && t.getDate().getMonthValue() == month
                        && t.getDate().getYear() == year
                        && t.getType().name().equals("INCOME"))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = transactions.stream()
                .filter(t -> t.getDate() != null
                        && t.getDate().getMonthValue() == month
                        && t.getDate().getYear() == year
                        && t.getType().name().equals("EXPENSE"))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal savings = totalIncome.subtract(totalExpenses);

        return new MonthlySummaryDto(month, year, totalIncome, totalExpenses, savings);
    }

    public AnnualSummaryDto getAnnualSummary(long loggedInUserId, int year) {
        List<RecurringTransaction> recurringTransactions = recurringTransactionRepo.findByUserId(loggedInUserId);
        List<RecurringTransaction> annualRecurringTransactions = recurringTransactions.stream().filter(rt -> rt.getStartDate().getYear() == year).toList();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        BigDecimal totalSalary = BigDecimal.ZERO;
        for (var rt  : annualRecurringTransactions) {
            if (Objects.equals(rt .getCategory().getName(), "SALARY")) {
                totalSalary = totalSalary.add(rt .getAmount());
            }
            long occurrences = switch (rt.getFrequency()) {
                case WEEKLY -> ChronoUnit.WEEKS.between(rt.getStartDate(), rt.getEndDate().plusDays(1));
                case MONTHLY -> ChronoUnit.MONTHS.between(rt.getStartDate(), rt.getEndDate().plusDays(1));
                case HALF_ANNUAL -> rt.getStartDate().datesUntil(rt.getEndDate().plusDays(1), Period.ofMonths(6)).count();
                case ANNUAL -> 1;
                default -> 0;
            };
            BigDecimal totalAmount = rt.getAmount().multiply(BigDecimal.valueOf(occurrences));
            if (rt.getType() == CategoryTypeEnum.INCOME) {
                totalIncome = totalIncome.add(totalAmount);
            } else {
                totalExpense = totalExpense.add(totalAmount);
            }
        }
        BigDecimal savings = totalIncome.subtract(totalExpense);
        return new AnnualSummaryDto(year, totalIncome, totalExpense, totalSalary, savings.abs());
    }
    public BasicStatisticsDto basicStatistics(long loggedInUserId , int year , int month){
        List<Transaction> transactions = transactionRepo.findAll().stream().filter(t->t.getUser().getId() == loggedInUserId).toList();
        BigDecimal maxAmount = transactions.stream()
                .filter(t->t.getType() == CategoryTypeEnum.EXPENSE)
                .map(Transaction::getAmount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        String categoryName = transactions.stream().filter(t-> Objects.equals(t.getAmount(), maxAmount))
                .map(t->t.getCategory().getName())
                .findFirst()
                .orElse("No Category");
        BigDecimal averageMonthlySpending = (getAnnualSummary(loggedInUserId ,year).getTotalExpense()).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        BigDecimal lastMonthExpense = getMonthlySummary(loggedInUserId, month - 1, year).getTotalExpenses();
        BigDecimal thisMonthExpense = getMonthlySummary(loggedInUserId, month, year).getTotalExpenses();
        BigInteger percentageChangeFromLastMonth = calculatePercentageChange(lastMonthExpense, thisMonthExpense);

        return new BasicStatisticsDto(maxAmount,categoryName,averageMonthlySpending,percentageChangeFromLastMonth);

    }
    private BigInteger calculatePercentageChange(BigDecimal lastMonth, BigDecimal thisMonth) {
        if (lastMonth.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.toBigInteger(); // avoid division by zero
        }
        return thisMonth.subtract(lastMonth)
                .divide(lastMonth,RoundingMode.DOWN)
                .multiply(BigDecimal.valueOf(100)).toBigInteger().abs();
    }
}
