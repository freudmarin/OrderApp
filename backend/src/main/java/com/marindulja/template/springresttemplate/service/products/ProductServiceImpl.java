package com.marindulja.template.springresttemplate.service.products;

import com.marindulja.template.springresttemplate.dto.ProductDto;
import com.marindulja.template.springresttemplate.model.Category;
import com.marindulja.template.springresttemplate.model.Product;
import com.marindulja.template.springresttemplate.repository.CategoryRepository;
import com.marindulja.template.springresttemplate.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;
    private ModelMapper mapper = new ModelMapper();

    @Override
    public List<ProductDto> getAllProducts()  {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public ProductDto addProduct(ProductDto productToBeAdded) {
        Category category = categoryRepository.findById(productToBeAdded.getCategory().getId()).get();
        Product product = new Product();
        product.setProductCode(productToBeAdded.getProductCode());
        product.setProductName(productToBeAdded.getProductName());
        product.setDescription(productToBeAdded.getDescription());
        product.setCategory(category);
        product.setUnitPrice(productToBeAdded.getUnitPrice());
        product.setUnitInStock(productToBeAdded.getUnitInStock());
        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    @Override
    public ResponseEntity<ProductDto> getProductById(long id) {
        Optional<Product> productData = productRepository.findById(id);
        if (productData.isPresent()) {
            return new ResponseEntity<>(mapToDTO(productData.get()), HttpStatus.OK);
        } else {
            throw new com.marindulja.template.springresttemplate.exception.NotFoundException("Product was  not found");
        }
    }

    @Override
    public ResponseEntity<ProductDto> updateProductById(long id, ProductDto productDto) {
        Optional<Product> productData = productRepository.findById(id);
        if (productData.isPresent()) {
            Product product = productData.get();
            product.setProductCode(productDto.getProductCode());
            product.setProductName(productDto.getProductName());
            product.setDescription(productDto.getDescription());
            product.setCategory(categoryRepository.findById(productDto.getCategory().getId()).get());
            product.setUnitPrice(productDto.getUnitPrice());
            product.setUnitInStock(productDto.getUnitInStock());
            Product updatedProduct = productRepository.save(product);
            return new ResponseEntity<>(mapToDTO(updatedProduct), HttpStatus.OK);
        } else {
            throw new com.marindulja.template.springresttemplate.exception.NotFoundException("Product was  not found");
        }
    }

    @Override
    public ResponseEntity<HttpStatus> deleteProductById(long id) {
        try {
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new com.marindulja.template.springresttemplate.exception.NotFoundException("Product was  not found");
        }
    }

    private ProductDto mapToDTO(Product product) {
        return mapper.map(product, ProductDto.class);
    }

    private Product mapToEntity(ProductDto productDto) {
        return mapper.map(productDto, Product.class);
    }
}
