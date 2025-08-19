package com.NtgSummerTrainingApp.PersonalFinanceTracker.repository;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecurringTransactionRepo extends JpaRepository<RecurringTransaction, Long> {

    List<RecurringTransaction> findByActiveTrue();
    List<RecurringTransaction> findByUserId(long userId);
}
