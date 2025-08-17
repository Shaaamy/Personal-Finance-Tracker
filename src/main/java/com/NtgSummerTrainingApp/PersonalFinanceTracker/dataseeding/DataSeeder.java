package com.NtgSummerTrainingApp.PersonalFinanceTracker.dataseeding;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Category;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryTypeEnum;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Role;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.RoleEnum;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.CategoryRepository;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(CategoryRepository categoryRepository, RoleRepository roleRepository) {
        return args -> {
            // Role Seeding
            for(RoleEnum roleEnum : RoleEnum.values()) {
                if (roleRepository.findByName(roleEnum.name()).isEmpty()) {
                    Role role = new Role();
                    role.setName(RoleEnum.valueOf(String.valueOf(roleEnum)));
                    roleRepository.save(role);
                }
            }
            // Category Seeding
            List<Category> defaultCategories = List.of(
                    new Category("Food", CategoryTypeEnum.EXPENSE, null, null, null,null),
                    new Category("Transport", CategoryTypeEnum.EXPENSE, null, null, null, null),
                    new Category("Rent", CategoryTypeEnum.EXPENSE, null, null, null, null),
                    new Category( "Entertainment", CategoryTypeEnum.EXPENSE, null, null, null, null),
                    new Category( "Shopping", CategoryTypeEnum.EXPENSE, null, null, null, null),
                    new Category( "Salary", CategoryTypeEnum.INCOME, null, null, null, null),
                    new Category( "Freelance", CategoryTypeEnum.INCOME, null, null, null, null)
            );

            for (Category category : defaultCategories) {
                categoryRepository.findByName(category.getName())
                        .orElseGet(() -> categoryRepository.save(category));
            }
        };
    }
}
