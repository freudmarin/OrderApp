package com.marindulja.template.springresttemplate.service.orders;

import com.marindulja.template.springresttemplate.dto.OrderDto;
import com.marindulja.template.springresttemplate.dto.OrderItemDto;
import com.marindulja.template.springresttemplate.dto.OrderRequestResponse;
import com.marindulja.template.springresttemplate.dto.PlaceOrderDto;
import com.marindulja.template.springresttemplate.exception.OrderAppException;
import com.marindulja.template.springresttemplate.model.Order;
import com.marindulja.template.springresttemplate.model.OrderItem;
import com.marindulja.template.springresttemplate.model.Product;
import com.marindulja.template.springresttemplate.repository.CustomerRepository;
import com.marindulja.template.springresttemplate.repository.OrderRepository;
import com.marindulja.template.springresttemplate.repository.ProductRepository;
import com.marindulja.template.springresttemplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final OrderItemsService orderItemsService;

    private final CustomerRepository customerRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    @Transactional
    public void placeOrder(long customerID, long userId, List<OrderItemDto> orderItemsDto) throws OrderAppException {
        PlaceOrderDto placeOrderDto = new PlaceOrderDto();
        placeOrderDto.setUserId(userId);
        placeOrderDto.setCustomerId(customerID);
        Double totalPrice = 0.00;
        Order order = saveOrder(placeOrderDto);
        for (OrderItemDto orderItemDto : orderItemsDto) {
            Product product = productRepository.findById(orderItemDto.getProductId()).get();
            OrderItem orderItem = new OrderItem(
                    order,
                    product,
                    orderItemDto.getQuantity(),
                    orderItemDto.getPrice(),
                    orderItemDto.getDiscount());
            orderItemsService.addOrderedProducts(orderItem);

            if (product.getUnitInStock() - orderItemDto.getQuantity() > 0) {
                product.setUnitInStock(product.getUnitInStock() - orderItemDto.getQuantity());
                productRepository.save(product);
            } else {
                throw new OrderAppException(HttpStatus.BAD_REQUEST, "You cannot make this order, because there is not enough stock left");
            }
            totalPrice += orderItemDto.getPrice() * orderItemDto.getQuantity() - ((double) orderItemDto.getDiscount() /100 * orderItemDto.getPrice() * orderItem.getQuantity());
        }
        orderRepository.updateOrder(totalPrice, order.getId());
    }

    public Order saveOrder(PlaceOrderDto orderDto){
        Order order = getOrderFromDto(orderDto);
        return orderRepository.save(order);
    }

    private Order getOrderFromDto(PlaceOrderDto orderDto) {
        Order order = new Order(customerRepository.findById(orderDto.getCustomerId()).get(), userRepository.findById(orderDto.getUserId()).get());
        return order;
    }

    public List<OrderRequestResponse> listAllOrders() {
        List<OrderRequestResponse> orderList = orderRepository.findAll().stream()
                .map(order ->
                        new OrderRequestResponse(mapOrderToOrderDto(order) ,
                                order.getOrderDetails().stream().map(this::mapOrderItemsToOrderItemsDto).collect(Collectors.toList())))
                .collect(Collectors.toList());
        return orderList;
    }

    public List<OrderRequestResponse> listOrders(Long user_id) {
        List<OrderRequestResponse> orderList = orderRepository.findAllByUserIdOrderByCreatedAtDesc(user_id).stream()
                .map(order ->
                        new OrderRequestResponse(mapOrderToOrderDto(order) ,
                                order.getOrderDetails().stream().map(this::mapOrderItemsToOrderItemsDto).collect(Collectors.toList())))
                                .collect(Collectors.toList());
        return orderList;
    }

    private OrderDto mapOrderToOrderDto(Order order) {
        return new OrderDto(order.getId(),
                order.getCustomer().getId());
    }

    private OrderItemDto mapOrderItemsToOrderItemsDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getUnitPrice(),
                orderItem.getQuantity(), orderItem.getProduct().getId(),
                orderItem.getDiscount());
    }
}
