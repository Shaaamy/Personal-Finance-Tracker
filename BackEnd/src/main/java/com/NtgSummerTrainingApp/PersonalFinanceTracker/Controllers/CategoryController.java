package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.CategoryService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.*;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<ApiResponse<String>> createCategory(@RequestBody @Valid CategoryRequestDto categoryReq) {
        String result = categoryService.createNewCategory(categoryReq);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Category created successfully", result));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategoryById(@PathVariable long id) {
        CategoryResponseDto category = categoryService.findCategoryById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category fetched successfully", category));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<PaginationDto<CategoryResponseDto>>> getAllCategories(@ModelAttribute PaginationRequest paginationReq) {
        PaginationDto<CategoryResponseDto> categories = categoryService.findAllCategories(paginationReq);
        return ResponseEntity.ok(new ApiResponse<>(true, "Categories fetched successfully", categories));
    }


    @GetMapping("/by-name/{name}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategoryByName(@PathVariable String name) {
        CategoryResponseDto category = categoryService.findCategoryByName(name);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category fetched successfully", category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateCategory(@Valid @RequestBody CategoryRequestDto categoryReq, @PathVariable long id) {
        String result = categoryService.update(categoryReq, id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category updated successfully", result));
    }

    @PatchMapping("/updatePartially/{id}")
    public ResponseEntity<ApiResponse<String>> updateCategoryPartially(@RequestBody CategoryRequestDto categoryReq, @PathVariable long id) {
        String result = categoryService.updateCategoryPartially(categoryReq, id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category partially updated successfully", result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable long id) {
        String result = categoryService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category deleted successfully", result));
    }
}
