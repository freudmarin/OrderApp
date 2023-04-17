package com.marindulja.template.springresttemplate.repository;

import com.marindulja.template.springresttemplate.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product,Long> {
    Optional<Product> findByProductCode(String productCode);

}
