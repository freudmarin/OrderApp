package com.marindulja.template.springresttemplate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long orderId;
    private Long customerId;
}
