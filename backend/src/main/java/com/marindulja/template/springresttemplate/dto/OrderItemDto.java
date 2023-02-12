package com.marindulja.template.springresttemplate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private  double price;
    private  int quantity;
    private  long productId;
    private  int discount;
}
