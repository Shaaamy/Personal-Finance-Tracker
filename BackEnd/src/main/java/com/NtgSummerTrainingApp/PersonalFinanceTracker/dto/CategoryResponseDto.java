package com.NtgSummerTrainingApp.PersonalFinanceTracker.dto;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Category;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponseDto {
    private long id;
    private String name;
    private CategoryTypeEnum type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.type = category.getType();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
    }
}
