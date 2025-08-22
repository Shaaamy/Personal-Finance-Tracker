package com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.CategoryRequestDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.CategoryResponseDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Category;
import org.springframework.data.domain.Page;

import java.util.List;

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

//    public static PaginationDto<> toPaginatedDto(Page<Category> categoryPage){
//            List<CategoryResponseDto> dtos = categoryPage.stream().map(CategoryMapper::toDTO).toList();
//            return new PaginationDto<>(
//                    dtos,
//                    categoryPage.getNumber(),
//                    categoryPage.getSize(),
//                    categoryPage.getTotalPages(),
//                    categoryPage.getTotalElements()
//            );
//    }
}
