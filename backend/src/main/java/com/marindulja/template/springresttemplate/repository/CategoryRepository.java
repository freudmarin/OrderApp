package com.marindulja.template.springresttemplate.repository;

import com.marindulja.template.springresttemplate.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
