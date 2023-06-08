package com.marindulja.orderapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private OrderResponseDto order;
    private List<OrderItemDto> items;
}
