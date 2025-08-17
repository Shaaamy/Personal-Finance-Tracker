package com.NtgSummerTrainingApp.PersonalFinanceTracker.dto;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Category;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.CategoryTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {
    @NotBlank(message = "Category Name is required")
    private String name;
    @NotNull(message = "Category Type is required")
    private CategoryTypeEnum type;


    public CategoryRequestDto(Category category) {
        this.name = category.getName();
        this.type = category.getType();
    }
}
