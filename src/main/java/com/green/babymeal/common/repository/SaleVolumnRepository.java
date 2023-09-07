package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.SaleVolumnEntity;
import com.green.babymeal.mypage.model.SaleVolumnCount;
import com.green.babymeal.mypage.model.SaleVolumnVo;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleVolumnRepository extends JpaRepository <SaleVolumnEntity, Long> {

//    @Query("select new com.green.babymeal.mypage.model.SaleVolumnVo(sum(A.count),A.productId.productId,A.productId.pName,B.pPrice)"+
//            " from SaleVolumnEntity A" +
//            " join ProductEntity B"+
//            " on A.productId.productId = B.productId"+
//            " group by A.productId.productId")
//    List<SaleVolumnVo>find();
//
//    SaleVolumnCount findByCreatedAtBetween(LocalDate start, LocalDate end);
}
