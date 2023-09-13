package com.green.babymeal.common.repository;

import com.green.babymeal.cate.model.CateSelVo;
import com.green.babymeal.common.entity.CategoryEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CateRepository extends JpaRepository<CategoryEntity,Long> {

    @Query("select new com.green.babymeal.cate.model.CateSelVo(" +
            "A.productId,C.img,A.pPrice,A.pName,A.pQuantity,A.saleVolume,A.pointRate) " +
            " from ProductEntity A" +
            " left join ProductCateRelationEntity B" +
            " on A.productId=B.productEntity.productId" +
            " left join ProductThumbnailEntity C" +
            " on A.productId=C.productId.productId" +
            " left join ProductAllergyEntity D" +
            " on A.productId=D.productId.productId" +
            " where B.categoryEntity.cateId=:cateId and A.pQuantity!=0 and A.isDelete=0 and C.img is not null " +
            "and B.cateDetailEntity.cateDetailId=:cateDetailId " +
            " group by A.productId")
    List<CateSelVo> findBy(@Param("cateId") Long cateId, @Param("cateDetailId") Long cateDetailId, Pageable pageable);

   @Query("select new com.green.babymeal.cate.model.CateSelVo(" +
           "A.productId,C.img,A.pPrice,A.pName,A.pQuantity,A.saleVolume,A.pointRate) " +
           " from ProductEntity A" +
           " left join ProductCateRelationEntity B" +
           " on A.productId=B.productEntity.productId" +
           " left join ProductThumbnailEntity C" +
           " on A.productId=C.productId.productId" +
           " left join ProductAllergyEntity D" +
           " on A.productId=D.productId.productId" +
           " where B.categoryEntity.cateId=:cateId and A.pQuantity!=0 and A.isDelete=0 and C.img is not null " +
           "and B.cateDetailEntity.cateDetailId=:cateDetailId " +
           " group by A.productId")
   Page<List<CateSelVo>> findByCount(@Param("cateId") Long cateId, @Param("cateDetailId") Long cateDetailId, Pageable pageable);

    @Query("select new com.green.babymeal.cate.model.CateSelVo(" +
            "A.productId,C.img,A.pPrice,A.pName,A.pQuantity,A.saleVolume,A.pointRate) " +
            " from ProductEntity A" +
            " left join ProductCateRelationEntity B" +
            " on A.productId=B.productEntity.productId" +
            " left join ProductThumbnailEntity C" +
            " on A.productId=C.productId.productId" +
            " left join ProductAllergyEntity D" +
            " on A.productId=D.productId.productId" +
            " where B.categoryEntity.cateId=:cateId and A.pQuantity!=0 and " +
            "A.isDelete=0 and C.img is not null" +
            " group by A.productId")
    List<CateSelVo> findBySel(@Param("cateId") Long cateId,Pageable pageable);


    @Query("select new com.green.babymeal.cate.model.CateSelVo(" +
            "A.productId,C.img,A.pPrice,A.pName,A.pQuantity,A.saleVolume,A.pointRate) " +
            " from ProductEntity A" +
            " left join ProductCateRelationEntity B" +
            " on A.productId=B.productEntity.productId" +
            " left join ProductThumbnailEntity C" +
            " on A.productId=C.productId.productId" +
            " left join ProductAllergyEntity D" +
            " on A.productId=D.productId.productId" +
            " where B.categoryEntity.cateId=:cateId and A.pQuantity!=0 and " +
            "A.isDelete=0 and C.img is not null" +
            " group by A.productId")
    Page<List<CateSelVo>> findBySelCount(@Param("cateId") Long cateId,Pageable pageable);

}
