package com.green.babymeal.orderbasket;

import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.OrderBasketRepository;
import com.green.babymeal.orderbasket.model.OrderBasketInsDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
            OrderBasketEntity orderBasketEntity = OrderBasketEntity.builder()
                    .count(existCheck.getCount() + dto.getCount())
                    .cartId(existCheck.getCartId())
                    .createAt(existCheck.getCreateAt())
                    .productEntity(productEntity)
                    .userEntity(userEntity)
                    .build();
            repository.save(orderBasketEntity);
        return  orderBasketEntity.getCartId();
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
