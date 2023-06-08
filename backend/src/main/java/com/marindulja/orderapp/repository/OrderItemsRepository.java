package com.marindulja.orderapp.repository;

import com.marindulja.orderapp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {

}
