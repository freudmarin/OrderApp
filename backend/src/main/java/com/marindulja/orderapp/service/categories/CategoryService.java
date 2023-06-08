package com.marindulja.orderapp.service.categories;

import com.marindulja.orderapp.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    Page<CategoryDto> getPaginatedAndFilteredCategories(Pageable pageRequest,
    String searchValue);
    List<CategoryDto> getAllCategories();
    CategoryDto addCategory(CategoryDto categoryToBeAdded);
    ResponseEntity<CategoryDto> getCategoryById(long id);
    ResponseEntity<CategoryDto> updateUserById(long id, CategoryDto category);
    void  deleteCategoryById(long id);
}
