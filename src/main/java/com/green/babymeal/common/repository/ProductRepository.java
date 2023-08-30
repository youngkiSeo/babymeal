package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.ProductEntity;
import com.green.babymeal.product.model.ProductVolumeDto;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {

    @Query("SELECT p.pName, SUM(p.saleVolume) AS totalSaleVolume " +
            "FROM ProductEntity p " +
            "WHERE YEAR(p.createdAt) = :year AND MONTH(p.createdAt) = :month " +
            "GROUP BY p.pName")
    List<ProductVolumeDto> findSaleVolume(@Param("year") int year, @Param("month") int month);// 월별 데이터 추출을 위함




}
