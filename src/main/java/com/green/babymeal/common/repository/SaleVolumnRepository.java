package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.SaleVolumnEntity;
import com.green.babymeal.mypage.model.SaleVolumnVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleVolumnRepository extends JpaRepository <SaleVolumnEntity, Long> {

    @Query("select new com.green.babymeal.mypage.model.SaleVolumnVo(sum(A.count),A.productId.productId,A.productId.pName)"+
            " from SaleVolumnEntity A" +
            " group by A.productId.productId")
    List<SaleVolumnVo>find();

    //List<SaleVolumnVo>findAllByCreatedAtBetween(LocalDate Start, LocalDate end);
}
