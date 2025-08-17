package com.NtgSummerTrainingApp.PersonalFinanceTracker.repository;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Role;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name);
}
