package com.marindulja.orderapp.service.products;

import com.marindulja.orderapp.dto.ProductDto;
import com.marindulja.orderapp.exception.NotFoundException;
import com.marindulja.orderapp.model.Category;
import com.marindulja.orderapp.model.Product;
import com.marindulja.orderapp.repository.CategoryRepository;
import com.marindulja.orderapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Override
    public Page<ProductDto> getPaginatedAndFilteredProducts(Pageable pageRequest, String searchValue) {
        Page<Product> pageResult = productRepository.findAll(pageRequest);
        List<ProductDto> productsDto = pageResult
                .stream()
                .filter(res -> res.getProductName().contains(searchValue) || res.getDescription().contains(searchValue))
                .filter(prod -> !prod.isDeleted())
                .map(this::mapToDTO)
                .collect(toList());
        return new PageImpl<>(productsDto, pageRequest, productsDto.size());
    }

    @Override
    public List<ProductDto> getAllProducts() {
        Iterable<Product> products = productRepository.findAll();
        return StreamSupport.stream(products.spliterator(), false).filter(prod -> !prod.isDeleted()).map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto addProduct(ProductDto productToBeAdded) {
        Optional<Category> category = categoryRepository.findById(productToBeAdded.getCategory().getId());
        if (category.isPresent()) {
            Product product = new Product();
            product.setProductCode(productToBeAdded.getProductCode());
            product.setProductName(productToBeAdded.getProductName());
            product.setDescription(productToBeAdded.getDescription());
            product.setCategory(category.get());
            product.setUnitPrice(productToBeAdded.getUnitPrice());
            product.setUnitInStock(productToBeAdded.getUnitInStock());
            product.setDiscount(productToBeAdded.getDiscount());
            Product savedProduct = productRepository.save(product);
            return mapToDTO(savedProduct);
        } else {
            throw new NotFoundException("Category not found");
        }
    }

    @Override
    public ResponseEntity<ProductDto> getProductById(long id) {
        Optional<Product> productData = productRepository.findById(id);
        if (productData.isPresent()) {
            return new ResponseEntity<>(mapToDTO(productData.get()), HttpStatus.OK);
        } else {
            throw new NotFoundException("Product was  not found");
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
            product.setCategory(categoryRepository.findById(productDto.getCategory().getId()).orElseThrow());
            product.setUnitPrice(productDto.getUnitPrice());
            product.setUnitInStock(productDto.getUnitInStock());
            product.setDiscount(productDto.getDiscount());
            Product updatedProduct = productRepository.save(product);
            return new ResponseEntity<>(mapToDTO(updatedProduct), HttpStatus.OK);
        } else {
            throw new NotFoundException("Product was not found");
        }
    }

    @Override
    public void deleteProductById(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    public boolean isProductNotUnique(String productCode) {
        Optional<Product> productData = productRepository.findByProductCode(productCode).stream().filter(prod -> !prod.isDeleted()).findFirst();
        if (productData.isPresent()) {
            return true;
        }
        return false;
    }


    private ProductDto mapToDTO(Product product) {
        return mapper.map(product, ProductDto.class);
    }

    private Product mapToEntity(ProductDto productDto) {
        return mapper.map(productDto, Product.class);
    }
}
