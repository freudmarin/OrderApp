package com.marindulja.template.springresttemplate.repository;

import com.marindulja.template.springresttemplate.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
    Page<Order> findAllByUserIdOrderByCreatedAtDesc(Long user_id, Pageable pageRequest);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Order o SET o.totalPrice = :totalPrice WHERE o.id = :orderId")
    void updateOrder(@Param("totalPrice") double totalPrice, @Param("orderId") Long orderId);
}
