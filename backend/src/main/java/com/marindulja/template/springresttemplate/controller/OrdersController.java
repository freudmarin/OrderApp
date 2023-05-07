package com.marindulja.template.springresttemplate.controller;

import com.marindulja.template.springresttemplate.dto.OrderRequest;
import com.marindulja.template.springresttemplate.dto.OrderResponse;
import com.marindulja.template.springresttemplate.service.AuthService;
import com.marindulja.template.springresttemplate.service.orders.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/orders")
public class OrdersController {
    private final OrderService orderService;
    private final AuthService authService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin/paginated")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                            @RequestParam(name = "size", defaultValue = "5") Integer size) {
        return new ResponseEntity<>(orderService.getAllOrdersPaginated(page, size), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public ResponseEntity<Page<OrderResponse>> getOrdersByUser(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(name = "size", defaultValue = "5") Integer size) {
        Optional<User> currentUser = authService.getCurrentUser();
        return currentUser.map(user -> new ResponseEntity<>(orderService.getOrdersForUser(Long.valueOf(user.getUsername()), page, size), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("add")
    public ResponseEntity<Void> placeOrder(@RequestBody OrderRequest orderReq) {
        long userId = Long.parseLong(authService.getCurrentUser().get().getUsername());
        orderService.placeOrder(orderReq.getCustomerId(), userId, orderReq.getItems());
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
