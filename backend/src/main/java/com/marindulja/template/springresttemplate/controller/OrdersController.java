package com.marindulja.template.springresttemplate.controller;

import com.marindulja.template.springresttemplate.dto.OrderRequest;
import com.marindulja.template.springresttemplate.dto.OrderResponse;
import com.marindulja.template.springresttemplate.service.AuthService;
import com.marindulja.template.springresttemplate.service.orders.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/orders")
public class OrdersController {
    private final OrderService orderService;
    private final AuthService authService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("all/admin")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return new ResponseEntity<>(orderService.listAllOrders(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public ResponseEntity<List<OrderResponse>> getOrdersByUser() {
        return new ResponseEntity<>(orderService.listOrders(Long.valueOf(authService.getCurrentUser().get().getUsername())), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("add")
    public ResponseEntity<Void> placeOrder(@RequestBody OrderRequest orderReq) {
        long userId = Long.parseLong(authService.getCurrentUser().get().getUsername());
        orderService.placeOrder(orderReq.getCustomerId(), userId, orderReq.getItems());
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
