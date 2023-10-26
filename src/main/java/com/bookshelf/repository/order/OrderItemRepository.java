package com.bookshelf.repository.order;

import com.bookshelf.model.OrderItem;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Set<OrderItem> findAllByOrderId(Long orderId);
}
