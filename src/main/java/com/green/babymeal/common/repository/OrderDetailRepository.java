package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.OrderDetailEntity;
import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.mypage.model.OrderlistDetailVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
   List<OrderDetailEntity> findAllByOrderId(OrderlistEntity entity);

    @Query("select new com.green.babymeal.mypage.model.OrderlistDetailVo(C.productId,D.img,C.pName,A.createdAt,C.pPrice,B.count,B.totalPrice) " +
            " from OrderlistEntity A" +
            " left join OrderDetailEntity B" +
            " on A.orderId = B.orderId.orderId"+
            " left join ProductEntity C"+
            " on B.productId.productId = C.productId"+
            " left join ProductThumbnailEntity D"+
            " on D.productId.productId = C.productId"+
            " WHERE A.orderId=:orderId")
    List<OrderlistDetailVo>findByOrderId(@Param("orderId")Long orderId);
    List<OrderDetailEntity> findByOrderId_OrderCode(Long orderCode);
    List<OrderDetailEntity> findByOrderId_OrderCodeIn(List<Long> orderCodes);
}
