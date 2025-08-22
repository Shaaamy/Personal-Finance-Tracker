package com.NtgSummerTrainingApp.PersonalFinanceTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PersonalFinanceTrackerApplication {

	public static void main(String[] args) {
		System.out.println("Personal Finance Tracker App");
		SpringApplication.run(PersonalFinanceTrackerApplication.class, args);
	}

}
