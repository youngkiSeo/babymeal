package com.green.babymeal.orderbasket;

import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.OrderBasketRepository;
import com.green.babymeal.orderbasket.model.OrderBasketInsDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderBasketService {

    private final OrderBasketRepository repository;
    private final AuthenticationFacade USERPK;


    public Long post(OrderBasketInsDto dto) {
        ProductEntity productEntity = ProductEntity.builder()
                .productId(dto.getProductId())
                .build();
        UserEntity userEntity = UserEntity.builder()
                .iuser(USERPK.getLoginUser().getIuser())
                .build();


        OrderBasketEntity existCheck = repository.findByProductEntity_ProductIdAndUserEntity_Iuser(dto.getProductId(), USERPK.getLoginUser().getIuser());
        if (existCheck != null) {
            existCheck.setCount(existCheck.getCount() + dto.getCount());
            repository.save(existCheck);
            return existCheck.getCartId();
        } else {
            OrderBasketEntity orderBasketEntity = OrderBasketEntity.builder()
                    .productEntity(productEntity)
                    .userEntity(userEntity)
                    .count(dto.getCount())
                    .build();
            repository.save(orderBasketEntity);
        return orderBasketEntity.getCartId();


        }
    }
}
