package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.ProductAllergyEntity;
import com.green.babymeal.common.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAllergyRepository extends JpaRepository<ProductAllergyEntity, Long> {
    List<ProductAllergyEntity> findByProductId_ProductId(Long productId);
    // 메소드 이름 규칙때문에 언더바(_)붙였습니다 이거 없애면 동작안함!! 수정 XXX
}

