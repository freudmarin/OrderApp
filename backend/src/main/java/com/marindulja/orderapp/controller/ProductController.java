package com.marindulja.orderapp.controller;

import com.marindulja.orderapp.dto.ProductDto;
import com.marindulja.orderapp.service.products.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping("add")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.addProduct(productDto), HttpStatus.OK);
    }

    @GetMapping("paginated")
    public Page<ProductDto> getAllProductsPaginatedAndFiltered(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(name = "size", defaultValue = "5") Integer size,
                                                               @RequestParam(name = "searchValue") String searchValue) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productService.getPaginatedAndFilteredProducts(pageRequest, searchValue);
    }

    @GetMapping()
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") long id) {
        return productService.getProductById(id);
    }

    @GetMapping("validateCode/{code}")
    public boolean getProductByCode(@PathVariable("code") String code) {
        return productService.isProductNotUnique(code);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") long id, @RequestBody ProductDto product) {
        return productService.updateProductById(id, product);
    }

    @DeleteMapping("{id}")
    public void deleteProduct(@PathVariable("id") long id) {
        productService.deleteProductById(id);
    }
}
