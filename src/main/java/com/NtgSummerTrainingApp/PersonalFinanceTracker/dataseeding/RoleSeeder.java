package com.NtgSummerTrainingApp.PersonalFinanceTracker.dataseeding;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Role;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.RoleEnum;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    @Override
    public void run(String... args) throws Exception {

            for(RoleEnum roleEnum : RoleEnum.values()){
                if(roleRepository.findByName(roleEnum.name()).isEmpty()) {
                    Role role = new Role();
                    role.setName(RoleEnum.valueOf(String.valueOf(roleEnum)));
                    roleRepository.save(role);
                }

            }
        }
    }

