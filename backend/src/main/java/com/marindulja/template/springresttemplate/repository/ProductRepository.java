package com.marindulja.template.springresttemplate.repository;

import com.marindulja.template.springresttemplate.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
