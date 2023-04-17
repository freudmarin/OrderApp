package com.marindulja.template.springresttemplate.repository;

import com.marindulja.template.springresttemplate.model.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends PagingAndSortingRepository<Category,Long> {
}
