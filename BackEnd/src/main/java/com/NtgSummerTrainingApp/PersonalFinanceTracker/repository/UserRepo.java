package com.NtgSummerTrainingApp.PersonalFinanceTracker.repository;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByUsername(String username);
}
