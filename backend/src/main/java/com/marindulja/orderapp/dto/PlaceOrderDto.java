package com.marindulja.orderapp.dto;


import com.marindulja.orderapp.model.Order;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PlaceOrderDto {
    private Long id;
    private @NotNull Long userId;
    private @NotNull Long customerId;
   // private @NotNull Double totalPrice;

    public PlaceOrderDto() {
    }

    public PlaceOrderDto(Order order) {
        this.setId(order.getId());
        this.setUserId(order.getUser().getId());
        this.setCustomerId(order.getCustomer().getId());
      //  this.setTotalPrice(order.getTotalPrice());
    }
}
