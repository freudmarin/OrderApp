package com.marindulja.orderapp.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ProductDto {

    private Long id;
    @NotBlank(message = "Product code is mandatory")
    private String productCode;
    @NotBlank(message = "Product name is mandatory")
    private String productName;
    private String description;
    @NotBlank(message = "Category is mandatory")
    private CategoryDto category;
    @NotBlank(message = "Price is mandatory")
    private Long unitPrice;
    @NotBlank(message = "Stock number is mandatory")
    private Integer unitInStock;

    @NotBlank(message = "Discount is mandatory")
    private Integer discount;

}
