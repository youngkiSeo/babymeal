package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImageEntity,Long> {

    void deleteByProductId_ProductId(Long productId);
}