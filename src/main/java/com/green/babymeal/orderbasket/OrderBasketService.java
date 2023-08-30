package com.green.babymeal.orderbasket;

import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.OrderBasketRepository;
import com.green.babymeal.common.repository.ThumbnailRepository;
import com.green.babymeal.orderbasket.model.OrderBasketInsDto;
import com.green.babymeal.orderbasket.model.OrderBasketVo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderBasketService {

    private final OrderBasketRepository repository;
    private final ThumbnailRepository thumbnailRepository;
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



    public List<OrderBasketVo> get(){

        List<OrderBasketEntity> byUserEntityIuser = repository.findByUserEntity_Iuser(USERPK.getLoginUser().getIuser());
        List<OrderBasketVo> list=new ArrayList<>();
        for (OrderBasketEntity orderBasketEntity : byUserEntityIuser) {
            Long productId = orderBasketEntity.getProductEntity().getProductId();
            ProductThumbnailEntity productThumbnailEntity = thumbnailRepository.findById(productId).get();
            OrderBasketVo vo=OrderBasketVo.builder()
                    .cartId(orderBasketEntity.getCartId())
                    .productId(orderBasketEntity.getProductEntity().getProductId())
                    .productName(orderBasketEntity.getProductEntity().getPName())
                    .price(orderBasketEntity.getProductEntity().getPPrice())
                    .createdAt(orderBasketEntity.getCreateAt())
                    .thumbnail("http://192.168.0.144:5001/img/product/" + productId + "/" + productThumbnailEntity.getImg())
                    .build();
            list.add(vo);
        }

        return list;
    }


}
