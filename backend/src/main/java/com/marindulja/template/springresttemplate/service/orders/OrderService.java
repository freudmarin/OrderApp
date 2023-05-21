package com.marindulja.template.springresttemplate.service.orders;

import com.marindulja.template.springresttemplate.dto.OrderItemDto;
import com.marindulja.template.springresttemplate.dto.OrderResponse;
import com.marindulja.template.springresttemplate.dto.OrderResponseDto;
import com.marindulja.template.springresttemplate.dto.PlaceOrderDto;
import com.marindulja.template.springresttemplate.exception.OrderAppException;
import com.marindulja.template.springresttemplate.model.*;
import com.marindulja.template.springresttemplate.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;


    private final OrderItemsRepository orderItemsRepository;

    private final CustomerRepository customerRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    @Transactional
    public void placeOrder(long customerID, long userId, List<OrderItemDto> orderItemsDto) throws OrderAppException {
        PlaceOrderDto placeOrderDto = new PlaceOrderDto();
        placeOrderDto.setUserId(userId);
        placeOrderDto.setCustomerId(customerID);
        double totalPrice = 0.00;
        Order order = saveOrder(placeOrderDto);
        //has to be improved
        orderItemsRepository.saveAll(orderItemsDto.stream().map(o -> new OrderItem(order, productRepository.findByProductCode(o.getProductCode()).orElseThrow(), o.getQuantity(), productRepository.findByProductCode(o.getProductCode()).orElseThrow().getUnitPrice())).collect(Collectors.toList()));
        for (OrderItemDto orderItemDto : orderItemsDto) {
            Product product = productRepository.findByProductCode(orderItemDto.getProductCode()).orElseThrow();

            if (product.getUnitInStock() - orderItemDto.getQuantity() > 0) {
                product.setUnitInStock(product.getUnitInStock() - orderItemDto.getQuantity());
                productRepository.save(product);
            } else {
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
        Order order = new Order(customer, user);
        return order;
    }


    public Page<OrderResponse> getAllOrdersPaginated(int pageNumber, int pageSize, String searchValue) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Order> pageResult = orderRepository.findAll(pageRequest);
        List<OrderResponse> orderList = pageResult.stream().map(order -> new OrderResponse(mapOrderToOrderDto(order),
                order.getOrderDetails().stream().map(this::mapOrderItemsToOrderItemsDto).collect(Collectors.toList()))).filter(orderResponse -> orderResponse.getOrder().getCustomerName().contains(searchValue)
                || orderResponse.getItems().stream().anyMatch(itemDto ->
                itemDto.getProductCode().contains(searchValue))).collect(Collectors.toList());
        return new PageImpl<>(orderList, pageRequest, pageResult.getTotalElements());
    }

    public Page<OrderResponse> getOrdersPaginatedForUser(Long user_id, int pageNumber, int pageSize, String searchValue) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Order> pageResult = orderRepository.findAllByUserIdOrderByCreatedAtDesc(user_id, pageRequest);
        List<OrderResponse> orderList = pageResult.stream().map(order -> new OrderResponse(mapOrderToOrderDto(order),
                order.getOrderDetails().stream().map(this::mapOrderItemsToOrderItemsDto).collect(Collectors.toList())))
                .filter(orderResponse -> orderResponse.getOrder().getCustomerName().contains(searchValue) ||
                        orderResponse.getItems().stream().anyMatch(itemDto -> itemDto.getProductCode().contains(searchValue) ||
                                String.valueOf(itemDto.getPrice()).contains(searchValue) ||
                                String.valueOf(itemDto.getQuantity()).contains(searchValue))).collect(Collectors.toList());
        return new PageImpl<>(orderList, pageRequest, pageResult.getTotalElements());
    }

    private OrderResponseDto mapOrderToOrderDto(Order order) {
        return new OrderResponseDto(order.getId(), order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
    }

    private OrderItemDto mapOrderItemsToOrderItemsDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getProduct().getUnitPrice(), orderItem.getQuantity(), orderItem.getProduct().getProductCode());
    }
}
