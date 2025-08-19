package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.MonthlySummaryDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.AnnualSummaryDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryTypeEnum;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.FrequencyEnum;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.RecurringTransaction;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Transaction;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.RecurringTransactionRepo;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public MonthlySummaryDto getMonthlySummary(Long userId, Integer month, Integer year) {
        List<Transaction> transactions = transactionRepo.findByUserId(userId);

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

    public AnnualSummaryDto getAnnualSummary(long userId, int year) {
        List<RecurringTransaction> recurringTransactions = recurringTransactionRepo.findByUserId(userId);
        List<RecurringTransaction> annualRecurringTransactions = recurringTransactions.stream().filter(rt -> rt.getStartDate().getYear() == year).toList();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        BigDecimal totalSalary = BigDecimal.ZERO;
        for (var rt  : annualRecurringTransactions) {
            if (Objects.equals(rt .getCategory().getName(), "SALARY")) {
                totalSalary = totalSalary.add(rt .getAmount());
            }
            long occurrences = switch (rt.getFrequency()) {
                case WEEKLY -> ChronoUnit.WEEKS.between(rt.getStartDate(), rt.getEndDate()) + 1;
                case MONTHLY -> ChronoUnit.MONTHS.between(rt.getStartDate().withDayOfMonth(1), rt.getEndDate().withDayOfMonth(1)) + 1;
                case HALF_ANNUAL -> rt.getStartDate().datesUntil(rt.getEndDate(), Period.ofMonths(6)).count();
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
}
