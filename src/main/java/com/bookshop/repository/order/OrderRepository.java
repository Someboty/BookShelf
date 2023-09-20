package com.bookshop.repository.order;

import com.bookshop.model.Order;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.user.id = :userId")
    Set<Order> findAllByUserId(Long userId);
}
