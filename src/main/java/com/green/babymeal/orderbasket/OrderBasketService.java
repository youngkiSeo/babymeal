package com.green.babymeal.orderbasket;

import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.OrderBasketRepository;
import com.green.babymeal.common.repository.ProductCategoryRelationRepository;
import com.green.babymeal.common.repository.ThumbnailRepository;
import com.green.babymeal.orderbasket.model.OrderBasketCount;
import com.green.babymeal.orderbasket.model.OrderBasketInsDto;
import com.green.babymeal.orderbasket.model.OrderBasketVo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OrderBasketService {

    private final OrderBasketRepository repository;
    private final ThumbnailRepository thumbnailRepository;
    private final AuthenticationFacade USERPK;
    private final ProductCategoryRelationRepository productCategoryRelationRepository;


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
            ProductCateRelationEntity byProductEntity = productCategoryRelationRepository.findByProductEntity(byUserEntityIuser.listIterator().next().getProductEntity());
            ProductThumbnailEntity productThumbnailEntity = thumbnailRepository.findById(productId).get();
            String fullName="["+byProductEntity.getCategoryEntity().getCateId()+"단계]"+orderBasketEntity.getProductEntity().getPName();

            OrderBasketVo vo=OrderBasketVo.builder()
                    .cartId(orderBasketEntity.getCartId())
                    .productId(orderBasketEntity.getProductEntity().getProductId())
                    .productName(fullName)
                    .price(orderBasketEntity.getProductEntity().getPPrice())
                    .createdAt(orderBasketEntity.getCreateAt())
                    .count(orderBasketEntity.getCount())
                    .thumbnail("/img/product/" + productId + "/" + productThumbnailEntity.getImg())
                    .build();
            list.add(vo);
        }

        return list;
    }



    public int put(OrderBasketCount orderBasketCount){
        OrderBasketEntity orderBasketEntity = repository.findById(orderBasketCount.getCartId()).get();
        if(orderBasketCount.getCheck()==1){
            orderBasketEntity.setCount(orderBasketEntity.getCount()+1);
            repository.save(orderBasketEntity);
            return 1;
        }
        else {
          orderBasketEntity.setCount(orderBasketEntity.getCount()-1);
          repository.save(orderBasketEntity);
          return 1;
        }
    }


    @Transactional
    public int delete(Long cartId){
        repository.deleteById(cartId);
        Optional<OrderBasketEntity> byId = repository.findById(cartId);

        if(byId.isEmpty()){
            return 1;
        }
        else return 0;
    }
}
