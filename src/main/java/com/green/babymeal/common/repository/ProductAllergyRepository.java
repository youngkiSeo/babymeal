package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.ProductAllergyEntity;
import com.green.babymeal.common.entity.ProductCateRelationEntity;
import com.green.babymeal.common.entity.ProductEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAllergyRepository extends JpaRepository<ProductAllergyEntity, Long> {
    List<ProductAllergyEntity> findByProductId(ProductEntity productId);
    List<ProductAllergyEntity> findByProductId_ProductId(Long productId);
    @Query("SELECT pa.allergyId.allergyId FROM ProductAllergyEntity pa WHERE pa.productId.productId = :productId")
    List<Long> findAllergyIdsByProductId(@Param("productId") Long productId);

}

