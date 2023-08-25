package com.green.babymeal.common.repository;

import com.green.babymeal.cate.model.CateSelList;
import com.green.babymeal.cate.model.CateSelSel;
import com.green.babymeal.common.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CateRepository extends JpaRepository<CategoryEntity,Long> {

    @Query("select p.productId as productId,p.allergy as allergy,i.img as img from ProductEntity p" +
            " join ProductImageEntity i " +
            " on p.productId=i.productId.productId")
    List<CateSelSel> findBy(CateSelList cateSelList );

}
