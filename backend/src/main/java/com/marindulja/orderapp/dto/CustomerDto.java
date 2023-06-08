package com.marindulja.orderapp.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CustomerDto {

    private Long id;

    @NotBlank(message = "Customer first  name is mandatory")
    private String firstName;

    @NotBlank(message = "Customer last name is mandatory")
    private String lastName;

    @NotBlank(message = "Customer adress is mandatory")
    private String address;
}
