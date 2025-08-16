package com.NtgSummerTrainingApp.PersonalFinanceTracker.repository;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Role;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;


public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByRole(RoleEnum role);
}
