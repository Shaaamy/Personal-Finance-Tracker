package com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.CategoryRequestDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.CategoryResponseDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Category;

public class CategoryMapper {

    public static CategoryResponseDto toDTO(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    public static Category toEntity(CategoryRequestDto dto) {
        return Category.builder()
                .name(dto.getName())
                .type(dto.getType())
                .build();
    }
}
