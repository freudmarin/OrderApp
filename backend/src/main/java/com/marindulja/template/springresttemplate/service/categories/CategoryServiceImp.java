package com.marindulja.template.springresttemplate.service.categories;

import com.marindulja.template.springresttemplate.dto.CategoryDto;
import com.marindulja.template.springresttemplate.model.Category;
import com.marindulja.template.springresttemplate.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private ModelMapper mapper = new ModelMapper();

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryToBeAdded) {
        Category category = new Category();
        category.setName(categoryToBeAdded.getName());
        Category savedCategory = categoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    @Override
    public ResponseEntity<CategoryDto> getCategoryById(long id) {
        Optional<Category> categoryData = categoryRepository.findById(id);
        if (categoryData.isPresent()) {
            return new ResponseEntity<>(mapToDTO(categoryData.get()), HttpStatus.OK);
        } else {
            throw new com.marindulja.template.springresttemplate.exception.NotFoundException("User not found");
        }
    }

    @Override
    public ResponseEntity<CategoryDto> updateUserById(long id, CategoryDto categoryDto) {
        Optional<Category> categoryData = categoryRepository.findById(id);
        if (categoryData.isPresent()) {
            Category category = categoryData.get();
            category.setName(categoryDto.getName());
            Category updatedCategory = categoryRepository.save(category);
            return new ResponseEntity<>(mapToDTO(updatedCategory), HttpStatus.OK);
        } else {
            throw new com.marindulja.template.springresttemplate.exception.NotFoundException("User not found");
        }
    }

    @Override
    public ResponseEntity<HttpStatus> deleteCategoryById(long id) {
        try {
            categoryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new com.marindulja.template.springresttemplate.exception.NotFoundException("User not found");
        }
    }

    private CategoryDto mapToDTO(Category category) {
        CategoryDto categoryDto = mapper.map(category, CategoryDto.class);
        return categoryDto;
    }

    private Category mapToEntity(CategoryDto categoryDto) {
        Category category = mapper.map(categoryDto, Category.class);
        return category;
    }
}
