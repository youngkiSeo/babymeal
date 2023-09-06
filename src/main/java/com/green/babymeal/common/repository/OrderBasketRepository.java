package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.OrderBasketEntity;
import com.green.babymeal.common.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderBasketRepository extends JpaRepository<OrderBasketEntity,Long> {

    OrderBasketEntity findByProductEntity_ProductIdAndUserEntity_Iuser(Long productId, Long iuser);


    List<OrderBasketEntity> findByUserEntity_Iuser(Long iuser);

    void deleteByUserEntity_Iuser(Long iuser);

}
