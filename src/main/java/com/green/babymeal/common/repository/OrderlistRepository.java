package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.common.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderlistRepository extends JpaRepository<OrderlistEntity, Long> {
    List<OrderlistEntity> findAllByIuser(UserEntity iuser);
}
