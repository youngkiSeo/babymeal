package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.ProductEntity;
import com.green.babymeal.common.entity.ProductImageEntity;
import com.green.babymeal.common.entity.ProductThumbnailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductThumbnailRepository extends JpaRepository<ProductThumbnailEntity,Long> {
    List<ProductThumbnailEntity> findByProductId(ProductEntity productEntity);
}
