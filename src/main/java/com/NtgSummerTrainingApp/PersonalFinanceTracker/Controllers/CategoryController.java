package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.CategoryService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.CategoryRequestDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.CategoryResponseDto;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<String> createCategory(@RequestBody @Valid CategoryRequestDto categoryReq){
        return new ResponseEntity<>( categoryService.createNewCategory(categoryReq), HttpStatus.CREATED);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable long id){
        return new ResponseEntity<>(categoryService.findCategoryById(id),HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(){
        return new ResponseEntity<>(categoryService.findAllCategories(),HttpStatus.OK);
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryResponseDto> getCategoryByName(@PathVariable String name){
        return new ResponseEntity<>(categoryService.findCategoryByName(name),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@Valid @RequestBody CategoryRequestDto categoryReq , @PathVariable long id){
        return new ResponseEntity<>(categoryService.update(categoryReq,id),HttpStatus.OK);
    }
    @PatchMapping("/updatePartially/{id}")
    public ResponseEntity<String> updateCategoryPartially( @RequestBody CategoryRequestDto categoryReq , @PathVariable long id){
        return new ResponseEntity<>(categoryService.updateCategoryPartially(categoryReq,id),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id){
        return new ResponseEntity<>(categoryService.delete(id),HttpStatus.OK);
    }
}
