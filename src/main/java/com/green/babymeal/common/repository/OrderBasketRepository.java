package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.OrderBasketEntity;
import com.green.babymeal.common.entity.OrderlistEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderBasketRepository extends JpaRepository<OrderBasketEntity,Long> {

    OrderBasketEntity findByProductEntity_ProductIdAndUserEntity_Iuser(Long productId, Long iuser);

    List<OrderBasketEntity> findByUserEntity_Iuser(Long iuser);

    void deleteAllByUserEntity_Iuser(Long iuser);

}
