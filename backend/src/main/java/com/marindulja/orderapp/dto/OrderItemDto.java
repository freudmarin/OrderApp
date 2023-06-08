package com.marindulja.orderapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemDto {
    private Double price;
    private  int quantity;
    private  String productCode;
}
