package com.NtgSummerTrainingApp.PersonalFinanceTracker.repository;


import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryTypeEnum;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction,Long> {
    List<Transaction> findByUserId(Long userId);
}
