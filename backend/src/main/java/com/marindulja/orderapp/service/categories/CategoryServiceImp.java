package com.marindulja.orderapp.service.categories;

import com.marindulja.orderapp.repository.CategoryRepository;
import com.marindulja.orderapp.dto.CategoryDto;
import com.marindulja.orderapp.exception.NotFoundException;
import com.marindulja.orderapp.model.Category;
import com.marindulja.orderapp.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Override
    public Page<CategoryDto> getPaginatedAndFilteredCategories(Pageable pageRequest, String searchValue) {
        Page<Category> pageResult = categoryRepository.findAll(pageRequest);
        List<CategoryDto> categoriesDto = pageResult
                .stream()
                .filter(res -> res.getName().contains(searchValue))
                .filter(category -> !category.isDeleted())
                .map(this::mapToDTO)
                .collect(toList());
        return new PageImpl<>(categoriesDto, pageRequest, pageResult.getTotalElements());
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        Iterable<Category> categories = categoryRepository.findAll();
        return StreamSupport.stream(categories.spliterator(), false)
                .filter(category -> !category.isDeleted())
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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
            throw new NotFoundException("User not found");
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
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public void  deleteCategoryById(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        category.setDeleted(true);
        for (Product product : category.getProducts()) {
            product.setDeleted(true);
        }
        categoryRepository.save(category);
    }

    private CategoryDto mapToDTO(Category category) {
        return mapper.map(category, CategoryDto.class);
    }

    private Category mapToEntity(CategoryDto categoryDto) {
        return mapper.map(categoryDto, Category.class);
    }
}
