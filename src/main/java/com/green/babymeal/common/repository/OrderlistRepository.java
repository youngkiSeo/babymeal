package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.common.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderlistRepository extends JpaRepository<OrderlistEntity, Long> {
    List<OrderlistEntity> findAllByIuser(UserEntity iuser);
    OrderlistEntity findByOrderid(Long orderId);


}
