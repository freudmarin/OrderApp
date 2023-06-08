package com.marindulja.orderapp.repository;

import com.marindulja.orderapp.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product,Long> {
    List<Product> findByProductCode(String productCode);

}
