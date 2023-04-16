package com.marindulja.template.springresttemplate.repository;

import com.marindulja.template.springresttemplate.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Order o SET o.totalPrice = :totalPrice WHERE o.id = :orderId")
    void updateOrder(@Param("totalPrice") double totalPrice, @Param("orderId") Long orderId);
}
