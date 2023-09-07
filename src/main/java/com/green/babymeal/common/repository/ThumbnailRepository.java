package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.OrderDetailEntity;
import com.green.babymeal.common.entity.ProductEntity;
import com.green.babymeal.common.entity.ProductThumbnailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbnailRepository extends JpaRepository<ProductThumbnailEntity,Long> {
    ProductThumbnailEntity findAllByProductId(ProductEntity entity);
    ProductThumbnailEntity findDistinctByProductId(ProductEntity entity);
}
