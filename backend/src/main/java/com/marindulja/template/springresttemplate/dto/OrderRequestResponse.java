package com.marindulja.template.springresttemplate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderRequestResponse {
    private OrderDto order;
    private List<OrderItemDto> items;
}
