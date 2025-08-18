package com.NtgSummerTrainingApp.PersonalFinanceTracker.repository;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecurringTransactionRepo extends JpaRepository<RecurringTransaction, Long> {
}
