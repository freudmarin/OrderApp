package com.marindulja.orderapp.service.orders;

import com.marindulja.orderapp.dto.PlaceOrderDto;
import com.marindulja.orderapp.model.*;
import com.marindulja.orderapp.repository.*;
import com.marindulja.orderapp.service.AuthService;
import com.marindulja.orderapp.adapters.UserAdapter;
import com.marindulja.orderapp.dto.OrderItemDto;
import com.marindulja.orderapp.dto.OrderResponse;
import com.marindulja.orderapp.dto.OrderResponseDto;
import com.marindulja.orderapp.exception.NotFoundException;
import com.marindulja.orderapp.exception.OrderAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;


    private final OrderItemsRepository orderItemsRepository;

    private final CustomerRepository customerRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final AuthService authService;

    @Transactional
    public void placeOrder(long customerID, long userId, List<OrderItemDto> orderItemsDto) throws OrderAppException {
        PlaceOrderDto placeOrderDto = new PlaceOrderDto();
        placeOrderDto.setUserId(userId);
        placeOrderDto.setCustomerId(customerID);
        double totalPrice = 0.00;
        Order order = saveOrder(placeOrderDto);
        //has to be improved
        orderItemsRepository.saveAll(orderItemsDto.stream().map(o -> new OrderItem(order, productRepository.findByProductCode(o.getProductCode()).stream().filter(prod -> !prod.isDeleted()).findFirst().get(),
                o.getQuantity(), productRepository.findByProductCode(o.getProductCode()).stream().filter(prod -> !prod.isDeleted()).findFirst().get().getUnitPrice())).collect(Collectors.toList()));
        for (OrderItemDto orderItemDto : orderItemsDto) {
            Product product = productRepository.findByProductCode(orderItemDto.getProductCode()).stream().filter(prod -> !prod.isDeleted()).findFirst().get();

            if (product.getUnitInStock() - orderItemDto.getQuantity() > 0) {
                product.setUnitInStock(product.getUnitInStock() - orderItemDto.getQuantity());
                productRepository.save(product);
            } else {
                log.info("You cannot make this order, because there is not enough stock left");
                throw new OrderAppException(HttpStatus.BAD_REQUEST, "You cannot make this order, because there is not enough stock left");
            }
            totalPrice += product.getUnitPrice() * orderItemDto.getQuantity() - ((double) product.getDiscount() / 100 * product.getUnitPrice() * orderItemDto.getQuantity());
        }
        orderRepository.updateOrder(totalPrice, order.getId());
    }

    public Order saveOrder(PlaceOrderDto orderDto) {
        Order order = getOrderFromDto(orderDto);
        return orderRepository.save(order);
    }

    private Order getOrderFromDto(PlaceOrderDto orderDto) {
        Customer customer = customerRepository.findById(orderDto.getCustomerId()).orElseThrow();
        User user = userRepository.findById(orderDto.getUserId()).orElseThrow();
        return new Order(customer, user);
    }


    public Page<OrderResponse> getOrdersPaginated(int pageNumber, int pageSize, String searchValue) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Order> pageResult;
        Optional<UserAdapter> currentUser = authService.getCurrentUser();
        String role = currentUser.get().getAuthorities().
                stream().map(grantedAuthority -> StringUtils.remove(grantedAuthority.getAuthority(), "ROLE_")).findFirst().get();
        if (role.equals(Role.USER.name())) {
            pageResult = orderRepository.findAllByUserIdOrderByCreatedAtDesc(userRepository.findByUsername(currentUser.get().getUsername()).get().getId(), pageRequest);
        } else {
            pageResult = orderRepository.findAll(pageRequest);
        }

        List<OrderResponse> orderList = pageResult.stream().map(order -> new OrderResponse(mapOrderToOrderDto(order),
                        order.getOrderDetails().stream().map(this::mapOrderItemsToOrderItemsDto).collect(Collectors.toList())))
                .filter(orderResponse -> orderResponse.getOrder().getCustomerName().contains(searchValue) ||
                        orderResponse.getItems().stream().anyMatch(itemDto -> itemDto.getProductCode().contains(searchValue) ||
                                (
                                        String.valueOf(itemDto.getPrice()).contains(searchValue) ||
                                                String.valueOf(itemDto.getQuantity()).contains(searchValue))
                        )).collect(Collectors.toList());

        return new PageImpl<>(orderList, pageRequest, pageResult.getTotalElements());
    }

    public OrderResponse getOrderByID(int orderId) {
        Order order = orderRepository.findById((long) orderId).orElseThrow(() -> new NotFoundException("Order was not found"));
        return new OrderResponse(new OrderResponseDto(order.getId(), order.getCustomer().getFirstName() +
                " " + order.getCustomer().getLastName()), order.getOrderDetails().stream().
                map(this::mapOrderItemsToOrderItemsDto).collect(Collectors.toList()));
    }

    private OrderResponseDto mapOrderToOrderDto(Order order) {
        return new OrderResponseDto(order.getId(), order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
    }

    private OrderItemDto mapOrderItemsToOrderItemsDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getProduct().getUnitPrice(), orderItem.getQuantity(), orderItem.getProduct().getProductCode());
    }
}
