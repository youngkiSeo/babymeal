package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.OrderDetailEntity;
import com.green.babymeal.common.entity.ProductEntity;
import com.green.babymeal.common.entity.ProductThumbnailEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThumbnailRepository extends JpaRepository<ProductThumbnailEntity,Long> {
    List<ProductThumbnailEntity> findAllByProductId(ProductEntity entity);


    @Query("select t from ProductThumbnailEntity t" +
            " where t.productId.productId=:productId" +
            " group by t.productId.productId")
    ProductThumbnailEntity findDistinctByProductId(@Param("productId") Long productId);
}
