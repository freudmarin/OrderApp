package com.marindulja.orderapp.service.products;

import com.marindulja.orderapp.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    Page<ProductDto> getPaginatedAndFilteredProducts(Pageable pageRequest, String searchValue);

    List<ProductDto> getAllProducts();
    ProductDto addProduct(ProductDto productToBeAdded);
    ResponseEntity<ProductDto> getProductById(long id);
    ResponseEntity<ProductDto> updateProductById(long id, ProductDto product);
    void deleteProductById(long id);
    boolean isProductNotUnique(String productCode);
}
