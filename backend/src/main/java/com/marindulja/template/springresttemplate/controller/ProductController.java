package com.marindulja.template.springresttemplate.controller;

import com.marindulja.template.springresttemplate.dto.ProductDto;
import com.marindulja.template.springresttemplate.service.products.ProductService;
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
    public Page<ProductDto> getAllProductsPaginated(@RequestParam(name="page", defaultValue = "0") Integer page,
                                                    @RequestParam(name="size", defaultValue = "5") Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productService.getPaginatedProducts(pageRequest);
    }

    @GetMapping()
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }
    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") long id) {
        return productService.getProductById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") long id, @RequestBody ProductDto product) {
        return productService.updateProductById(id, product);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") long id) {
        return productService.deleteProductById(id);
    }
}
