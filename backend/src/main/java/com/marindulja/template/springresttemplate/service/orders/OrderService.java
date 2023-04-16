package com.marindulja.template.springresttemplate.service.orders;

import com.marindulja.template.springresttemplate.dto.OrderItemDto;
import com.marindulja.template.springresttemplate.dto.OrderResponse;
import com.marindulja.template.springresttemplate.dto.OrderResponseDto;
import com.marindulja.template.springresttemplate.dto.PlaceOrderDto;
import com.marindulja.template.springresttemplate.exception.OrderAppException;
import com.marindulja.template.springresttemplate.model.Order;
import com.marindulja.template.springresttemplate.model.OrderItem;
import com.marindulja.template.springresttemplate.model.Product;
import com.marindulja.template.springresttemplate.repository.*;
import lombok.RequiredArgsConstructor;
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
        Double totalPrice = 0.00;
        Order order = saveOrder(placeOrderDto);
        //has to be improved
        orderItemsRepository.saveAll(orderItemsDto.stream().map(o-> new OrderItem(
                order,
                productRepository.findByProductCode(o.getProductCode()).get(),
                o.getQuantity(),
                productRepository.findByProductCode(o.getProductCode()).get().getUnitPrice())).collect(Collectors.toList()));
        for (OrderItemDto orderItemDto : orderItemsDto) {
            Product product = productRepository.findByProductCode(orderItemDto.getProductCode()).get();

            if (product.getUnitInStock() - orderItemDto.getQuantity() > 0) {
                product.setUnitInStock(product.getUnitInStock() - orderItemDto.getQuantity());
                productRepository.save(product);
            } else {
                throw new OrderAppException(HttpStatus.BAD_REQUEST, "You cannot make this order, because there is not enough stock left");
            }
            totalPrice += product.getUnitPrice() * orderItemDto.getQuantity() - ((double) product.getDiscount() /100 * product.getUnitPrice() * orderItemDto.getQuantity());
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

    public List<OrderResponse> listAllOrders() {
        List<OrderResponse> orderList = orderRepository.findAll().stream()
                .map(order ->
                        new OrderResponse(mapOrderToOrderDto(order) ,
                                order.getOrderDetails().stream().map(this::mapOrderItemsToOrderItemsDto).collect(Collectors.toList())))
                .collect(Collectors.toList());
        return orderList;
    }

    public List<OrderResponse> listOrders(Long user_id) {
        List<OrderResponse> orderList = orderRepository.findAllByUserIdOrderByCreatedAtDesc(user_id).stream()
                .map(order ->
                        new OrderResponse(mapOrderToOrderDto(order) ,
                                order.getOrderDetails().stream().map(this::mapOrderItemsToOrderItemsDto).collect(Collectors.toList())))
                                .collect(Collectors.toList());
        return orderList;
    }

    private OrderResponseDto mapOrderToOrderDto(Order order) {
        return new OrderResponseDto(order.getId(),
                order.getCustomer().getFirstName() +" " +order.getCustomer().getLastName());
    }

    private OrderItemDto mapOrderItemsToOrderItemsDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getProduct().getUnitPrice(), orderItem.getQuantity(), orderItem.getProduct().getProductCode());
    }
}
