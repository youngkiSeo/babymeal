package com.green.babymeal.common.repository;

import com.green.babymeal.admin.model.OrderlistDetailRes;
import com.green.babymeal.admin.model.OrderlistRes;
import com.green.babymeal.common.entity.OrderDetailEntity;
import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.common.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface OrderlistRepository extends JpaRepository<OrderlistEntity, Long> {
    List<OrderlistEntity> findAllByIuser(UserEntity iuser);
    OrderlistEntity findByOrderId(Long orderId);


    // 어드민
    Page<OrderlistEntity> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    @Query("SELECT A " +
            "FROM OrderlistEntity A " +
            "WHERE A.orderCode = :orderCode")
    List<OrderlistDetailRes> findOrderById(Long orderCode);
    OrderlistEntity findByOrderCode(Long orderCode); // 주문번호로 데이터 조회


}
