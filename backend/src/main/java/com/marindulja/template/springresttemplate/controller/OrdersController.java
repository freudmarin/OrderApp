package com.marindulja.template.springresttemplate.controller;

import com.marindulja.template.springresttemplate.dto.OrderRequestResponse;
import com.marindulja.template.springresttemplate.service.AuthService;
import com.marindulja.template.springresttemplate.service.orders.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    private final OrderService orderService;
    private final AuthService authService;

    @GetMapping("/all/admin")
    public ResponseEntity<List<OrderRequestResponse>> getAllOrders() {
        return new ResponseEntity<>(orderService.listAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/myOrders")
    public ResponseEntity<List<OrderRequestResponse>> getOrdersByUser() {
        return new ResponseEntity<>(orderService.listOrders(Long.valueOf(authService.getCurrentUser().get().getUsername())), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> placeOrder(@RequestBody OrderRequestResponse orderReq) {
        Long userId = Long.valueOf(authService.getCurrentUser().get().getUsername());
        orderService.placeOrder(orderReq.getOrder().getCustomerId(), userId, orderReq.getItems());
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
