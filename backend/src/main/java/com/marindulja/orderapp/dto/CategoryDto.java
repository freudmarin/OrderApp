package com.marindulja.orderapp.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CategoryDto {

    private Long id;

    @NotBlank(message = "Category name is mandatory")
    private String name;
}
