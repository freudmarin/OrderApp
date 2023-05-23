package com.marindulja.template.springresttemplate.controller;

import com.marindulja.template.springresttemplate.adapters.UserAdapter;
import com.marindulja.template.springresttemplate.dto.OrderRequest;
import com.marindulja.template.springresttemplate.dto.OrderResponse;
import com.marindulja.template.springresttemplate.repository.UserRepository;
import com.marindulja.template.springresttemplate.service.AuthService;
import com.marindulja.template.springresttemplate.service.orders.OrderService;
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
public class OrdersController {
    private final OrderService orderService;
    private final AuthService authService;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin/paginated")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                            @RequestParam(name = "size", defaultValue = "5") Integer size,
                                                            @RequestParam(name = "searchValue") String searchValue) {
        return new ResponseEntity<>(orderService.getAllOrdersPaginated(page, size, searchValue), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public ResponseEntity<Page<OrderResponse>> getOrdersByUser(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(name = "size", defaultValue = "5") Integer size,
                                                               @RequestParam(name = "searchValue") String searchValue) {
        Optional<UserAdapter> currentUser = authService.getCurrentUser();
        return currentUser.map(user -> new ResponseEntity<>
                (orderService.getOrdersPaginatedForUser(userRepository.findByUsername(currentUser.get().getUsername()).get().getId(), page, size, searchValue), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
}
