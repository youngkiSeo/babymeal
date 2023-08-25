package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.ProductCateRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRelationRepository extends JpaRepository<ProductCateRelationEntity, Long> {
}
