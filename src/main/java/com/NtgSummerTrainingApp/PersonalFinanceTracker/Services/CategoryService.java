package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.CategoryRequestDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.CategoryResponseDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Category;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
//import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@Data
public class CategoryService {
    private final CategoryRepository categoryRepository;

    //
     // Create new category
    //
    public String createNewCategory( CategoryRequestDto categoryReq){

        if(categoryRepository.existsByName(categoryReq.getName())){
            throw new RuntimeException("Category with name '" + categoryReq.getName() + "' already exists");
        }

        Category savedCategory = new Category(categoryReq.getName(),categoryReq.getType(),null,null,null,null);
        categoryRepository.save(savedCategory);
        return "New Category Is Added Successfully With Id : "+ savedCategory.getId();
    }

    //
     // Find Category By id
    //
    public CategoryResponseDto findCategoryById(long id){
        Category category = categoryRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Category with id " +id+" Not Found"));
        return new CategoryResponseDto(category);
    }

    //
     // Find All Categories
    //
    public List<CategoryResponseDto> findAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new EntityNotFoundException("No Categories Found");
        }
        return categories
                .stream()
                .map(CategoryResponseDto::new)
                .toList();
    }
}
