package com.marindulja.orderapp.repository;

import com.marindulja.orderapp.model.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends PagingAndSortingRepository<Category,Long> {
}
