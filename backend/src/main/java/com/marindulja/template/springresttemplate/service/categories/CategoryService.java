package com.marindulja.template.springresttemplate.service.categories;

import com.marindulja.template.springresttemplate.dto.CategoryDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto addCategory(CategoryDto categoryToBeAdded);
    ResponseEntity<CategoryDto> getCategoryById(long id);
    ResponseEntity<CategoryDto> updateUserById(long id, CategoryDto category);
    ResponseEntity<HttpStatus> deleteCategoryById(long id);
}
