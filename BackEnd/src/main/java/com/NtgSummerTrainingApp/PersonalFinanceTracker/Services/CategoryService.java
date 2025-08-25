package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.CategoryMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.PaginationMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.CategoryRequestDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.CategoryResponseDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationRequest;
import static com.NtgSummerTrainingApp.PersonalFinanceTracker.handler.BusinessExceptions.*;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.helper.PaginationHelper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.Category;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
//import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Data
public class CategoryService {
    private final CategoryRepository categoryRepository;

    //
     // Create new category
    //
    public String createNewCategory( CategoryRequestDto categoryReq){

        if(categoryRepository.existsByName(categoryReq.getName())){
            throw new DuplicateResourceException("Category with name '" + categoryReq.getName() + "' already exists");
        }

        Category savedCategory = CategoryMapper.toEntity(categoryReq);
        categoryRepository.save(savedCategory);
        return "New Category Is Added Successfully With Id : "+ savedCategory.getId();
    }

    //
     // Find Category By id
    //
    public CategoryResponseDto findCategoryById(long id){
        Category category = categoryRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Category with id " +id+" Not Found"));
        return CategoryMapper.toDTO(category);
    }

    //
     // Find All Categories
    //
    public PaginationDto<CategoryResponseDto> findAllCategories(PaginationRequest paginationReq){

        Page<Category> categories = categoryRepository.findAll(PaginationHelper.getPageable(paginationReq));
        if(categories.isEmpty()){
            throw new EntityNotFoundException("No Categories Found");
        }
        Page<CategoryResponseDto> categoryResponseDtoPage = categories.map(CategoryMapper::toDTO);
        return PaginationMapper.toPaginatedDto(categoryResponseDtoPage);
    }

    //
     // Find Category by Name
    //
    public CategoryResponseDto findCategoryByName(String name){
        Category category = categoryRepository.findByNameContainingIgnoreCase(name).orElseThrow(()->new EntityNotFoundException("Category with name " +name+" Not Found"));
        return CategoryMapper.toDTO(category);
    }

    //
     // Update Partially Category
    //
    public String updateCategoryPartially(CategoryRequestDto categoryReq , long id){
        Category existingCategory = categoryRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Category with id "+id +" Not Found"));
        if(categoryRepository.existsByName(categoryReq.getName())){
            throw new DuplicateResourceException("Category with name '" + categoryReq.getName() + "' already exists");
        }

        if (categoryReq.getName() != null ) {
            existingCategory.setName(categoryReq.getName());
        }
        if (categoryReq.getType() != null) {
            existingCategory.setType(categoryReq.getType());
        }
        categoryRepository.save(existingCategory);
        return "Category with id "+id+" is updated successfully";
    }

    //
     // Update Category
    //
    public String update(CategoryRequestDto categoryReq, long id) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Category with id "+id +" Not Found"));
        if(categoryRepository.existsByName(categoryReq.getName())){
            throw new DuplicateResourceException("Category with name '" + categoryReq.getName() + "' already exists");
        }
        existingCategory.setName(categoryReq.getName());
        existingCategory.setType(categoryReq.getType());
        categoryRepository.save(existingCategory);
        return "Category with id "+id+" is updated successfully";

    }
    //
     // Delete Category
    //
    public String delete(long id){
        Category existingCategory = categoryRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Category with id "+id +" Not Found"));
        categoryRepository.delete(existingCategory);
        return "Category with id "+id+" is Deleted successfully";
    }


}
