package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.OrderBasketEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderBasketRepository extends JpaRepository<OrderBasketEntity,Long> {

    OrderBasketEntity findByProductEntity_ProductIdAndUserEntity_Iuser(Long productId, Long iuser);
}
