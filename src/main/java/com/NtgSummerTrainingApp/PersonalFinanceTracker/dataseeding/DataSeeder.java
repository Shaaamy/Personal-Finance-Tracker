//package com.NtgSummerTrainingApp.PersonalFinanceTracker.dataseeding;
//
//import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Category;
//import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryType;
//import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.CategoryRepo;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//@Configuration
//public class DataSeeder {
//
//    @Bean
//    CommandLineRunner initDatabase(CategoryRepo categoryRepository) {
//        return args -> {
//            List<Category> defaultCategories = List.of(
//                    new Category(0L, "Food", CategoryType.EXPENSE, null, null, null),
//                    new Category(0L, "Transport", CategoryType.EXPENSE, null, null, null),
//                    new Category(0L, "Rent", CategoryType.EXPENSE, null, null, null),
//                    new Category(0L, "Entertainment", CategoryType.EXPENSE, null, null, null),
//                    new Category(0L, "Shopping", CategoryType.EXPENSE, null, null, null),
//                    new Category(0L, "Salary", CategoryType.INCOME, null, null, null),
//                    new Category(0L, "Freelance", CategoryType.INCOME, null, null, null)
//            );
//
//            for (Category category : defaultCategories) {
//                categoryRepository.findByName(category.getName())
//                        .orElseGet(() -> categoryRepository.save(category));
//            }
//        };
//    }
//}
