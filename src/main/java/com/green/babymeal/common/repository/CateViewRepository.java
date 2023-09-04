package com.green.babymeal.common.repository;

import com.green.babymeal.cate.model.CateViewRepositoryCate;
import com.green.babymeal.cate.model.CateViewRepositoryDetail;
import com.green.babymeal.common.entity.CateViewEntity;
import com.green.babymeal.common.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CateViewRepository extends JpaRepository<CateViewEntity,Long> {

    CateViewRepositoryCate findDistinctByCategoryEntity(CategoryEntity entity);
    List<CateViewRepositoryDetail> findByCategoryEntity_CateId(Long cateId);
}
