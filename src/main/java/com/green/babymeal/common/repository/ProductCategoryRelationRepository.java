package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.ProductCateRelationEntity;
import com.green.babymeal.common.entity.ProductEntity;
import com.green.babymeal.common.entity.ProductImageEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRelationRepository extends JpaRepository<ProductCateRelationEntity, Long> {
   List<ProductCateRelationEntity> findByProductEntity(ProductEntity entity);
   //ProductCateRelationEntity findFirstByProductEntity(ProductEntity entity);
   // 상품과 관련된 categoryEntity > cateDetailEntity >  cateDetailId를 가져오는 메소드
//   @Query("SELECT r.categoryEntity.cateId FROM ProductCateRelationEntity r WHERE r.productEntity = :productEntity")
//   Long findCateIdByProductEntity(Long productId);
   List<ProductCateRelationEntity> findByProductEntity_ProductId(Long productId);
   Optional<ProductCateRelationEntity> findFirstByProductEntity(ProductEntity entity); // 상품에 단계

   @Query("SELECT pcr.categoryEntity.cateId FROM ProductCateRelationEntity pcr WHERE pcr.productEntity.productId = :productId")
   Long findCateIdByProductId(@Param("productId") Long productId); // 상품 단계찾기


   @Query("select p from ProductCateRelationEntity p" +
           " where p.productEntity.productId=:productId " +
           " group by p.productEntity.productId ")
   ProductCateRelationEntity find(@Param("productId") Long productId);





}
