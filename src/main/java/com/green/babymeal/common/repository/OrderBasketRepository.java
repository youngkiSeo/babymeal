package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.OrderBasketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBasketRepository extends JpaRepository<OrderBasketEntity,Long> {

    OrderBasketEntity findByProductEntity_ProductIdAndUserEntity_Iuser(Long productId,Long iuser);
}
