package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    //List<OrderDetailEntity> findAllByOrderId(List<OrderlistEntity> orderId);
}
