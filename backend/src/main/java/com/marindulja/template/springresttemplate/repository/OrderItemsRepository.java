package com.marindulja.template.springresttemplate.repository;

import com.marindulja.template.springresttemplate.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {

}
