package com.marindulja.orderapp.controller;

import com.marindulja.orderapp.dto.CategoryDto;
import com.marindulja.orderapp.service.categories.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/categories")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("add")
    public ResponseEntity<CategoryDto> adCategory(@RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.addCategory(categoryDto), HttpStatus.OK);
    }

    @GetMapping()
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("paginated")
    public Page<CategoryDto> getAllCategoriesPaginated(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(name = "size", defaultValue = "5") Integer size,
                                                       @RequestParam(name = "searchValue") String searchValue) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return categoryService.getPaginatedAndFilteredCategories(pageRequest, searchValue);
    }


    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getUserById(@PathVariable("id") long id) {
        return categoryService.getCategoryById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateUser(@PathVariable("id") long id, @RequestBody CategoryDto category) {
        return categoryService.updateUserById(id, category);
    }

    @DeleteMapping("{id}")
    public void deleteCategory(@PathVariable("id") long id) {
        categoryService.deleteCategoryById(id);
    }
}
