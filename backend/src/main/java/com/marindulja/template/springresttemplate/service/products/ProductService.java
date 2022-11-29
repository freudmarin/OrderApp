package com.marindulja.template.springresttemplate.service.products;

import com.marindulja.template.springresttemplate.dto.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto addProduct(ProductDto productToBeAdded);
    ResponseEntity<ProductDto> getProductById(long id);
    ResponseEntity<ProductDto> updateProductById(long id, ProductDto product);
    ResponseEntity<HttpStatus> deleteProductById(long id);
}
