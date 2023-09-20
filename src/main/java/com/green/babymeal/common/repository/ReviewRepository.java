package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.ProductEntity;
import com.green.babymeal.common.entity.ReviewEntity;
import com.green.babymeal.product.model.ProductReviewSelDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Page<ReviewEntity> findAllByProductId(ProductEntity entity, Pageable pageable);
}