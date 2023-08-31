package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.ProductCateRelationEntity;
import com.green.babymeal.common.entity.ProductEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRelationRepository extends JpaRepository<ProductCateRelationEntity, Long> {
   ProductCateRelationEntity findByProductEntity(ProductEntity entity);
   // 상품과 관련된 categoryEntity > cateDetailEntity >  cateDetailId를 가져오는 메소드
//   @Query("SELECT r.categoryEntity.cateId FROM ProductCateRelationEntity r WHERE r.productEntity = :productEntity")
//   Long findCateIdByProductEntity(Long productId);
}
