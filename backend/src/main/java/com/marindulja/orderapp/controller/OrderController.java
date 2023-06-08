package com.marindulja.orderapp.controller;

import com.marindulja.orderapp.adapters.UserAdapter;
import com.marindulja.orderapp.dto.OrderRequest;
import com.marindulja.orderapp.dto.OrderResponse;
import com.marindulja.orderapp.repository.UserRepository;
import com.marindulja.orderapp.service.AuthService;
import com.marindulja.orderapp.service.orders.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;
    private final AuthService authService;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("paginated")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                            @RequestParam(name = "size", defaultValue = "5") Integer size,
                                                            @RequestParam(name = "searchValue") String searchValue) {
        return new ResponseEntity<>(orderService.getOrdersPaginated(page, size, searchValue), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("add")
    public ResponseEntity<Void> placeOrder(@RequestBody OrderRequest orderReq) {
        Optional<UserAdapter> currentUser = authService.getCurrentUser();
        if (currentUser.isPresent()) {
            long userId = userRepository.findByUsername(currentUser.get().getUsername()).get().getId();
            orderService.placeOrder(orderReq.getCustomerId(), userId, orderReq.getItems());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("{id}")
    public ResponseEntity<OrderResponse> getSpecificOrder(@PathVariable("id") Integer orderId) {
        return new ResponseEntity<>(orderService.getOrderByID(orderId), HttpStatus.OK);
    }
}
