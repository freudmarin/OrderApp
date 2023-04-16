package com.marindulja.template.springresttemplate.controller;

import com.marindulja.template.springresttemplate.dto.CategoryDto;
import com.marindulja.template.springresttemplate.service.categories.CategoryService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getUserById(@PathVariable("id") long id) {
        return categoryService.getCategoryById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateUser(@PathVariable("id") long id, @RequestBody CategoryDto category) {
        return categoryService.updateUserById(id, category);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        return categoryService.deleteCategoryById(id);
    }
}
