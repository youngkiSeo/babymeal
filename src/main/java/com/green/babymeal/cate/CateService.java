package com.green.babymeal.cate;

import com.green.babymeal.cate.model.*;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

import static com.green.babymeal.common.entity.QProductAllergyEntity.productAllergyEntity;
import static com.green.babymeal.common.entity.QProductCateRelationEntity.productCateRelationEntity;
import static com.green.babymeal.common.entity.QProductEntity.productEntity;
import static com.green.babymeal.common.entity.QProductThumbnailEntity.productThumbnailEntity;

@Service
@Slf4j
@RequiredArgsConstructor
public class CateService {

    private final CateRepository cateRepository;
    private final CateDetailRepository cateDetailRepository;
    private final CateViewRepository cateViewRepository;
    private final ProductCategoryRelationRepository productCategoryRelationRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final QProductEntity qProductEntity= productEntity;
    private final QProductCateRelationEntity qProductCateRelationEntity= productCateRelationEntity;
    private final QProductThumbnailEntity qProductThumbnailEntity= productThumbnailEntity;
    private final QProductAllergyEntity qProductAllergyEntity= productAllergyEntity;


    public List selCate() {
        List<CategoryEntity> all = cateRepository.findAll();
        List list = new ArrayList();

        for (int i = 0; i < all.size(); i++) {
            Category category = Category.builder()
                    .cateId(all.get(i).getCateId())
                    .cateName(all.get(i).getCateName())
                    .build();

            List<CateViewRepositoryDetail> byCategoryEntityCateId = cateViewRepository.findByCategoryEntity_CateId(all.get(i).getCateId());
            Cate build = Cate.builder()
                    .category(category)
                    .cateDetail(byCategoryEntityCateId)
                    .build();

            list.add(build);
        }
        return list;
    }


    public CateMaxPage selCateList(CateSelList cateSelList, Pageable pageable) {

        List<CateSelVo> by = cateRepository.findBy(cateSelList.getCateId(), cateSelList.getCateDetailId(), pageable);
        Page<List<CateSelVo>> byCount = cateRepository.findByCount(cateSelList.getCateId(), cateSelList.getCateDetailId(), pageable);


        if (null != cateSelList.getCateDetailId()) {
            for (int i = 0; i < by.size(); i++) {
                ProductCateRelationEntity productCateRelationEntities = productCategoryRelationRepository.find(by.get(i).getProductId());

                by.get(i).setName("[" + productCateRelationEntities.getCategoryEntity().getCateId() + "단계]" + by.get(i).getName());
                by.get(i).setThumbnail(/*"/img/product/" + by.get(i).getProductId() + "/" + */by.get(i).getThumbnail());
            }
            CateMaxPage cateMaxPage = new CateMaxPage();
            cateMaxPage.setList(by);
            cateMaxPage.setMaxPage(byCount.getTotalPages());
            cateMaxPage.setCount(byCount.getTotalElements());
            return cateMaxPage;

        } else {
            List<CateSelVo> bySel = cateRepository.findBySel(cateSelList.getCateId(), pageable);
            Page<List<CateSelVo>> bySelCount = cateRepository.findBySelCount(cateSelList.getCateId(), pageable);

            for (int i = 0; i < bySel.size(); i++) {
                ProductCateRelationEntity productCateRelationEntities = productCategoryRelationRepository.find(bySel.get(i).getProductId());


                bySel.get(i).setName("[" + productCateRelationEntities.getCategoryEntity().getCateId() + "단계]" + bySel.get(i).getName());
                bySel.get(i).setThumbnail(/*"/img/product/" + bySel.get(i).getProductId() + "/" + */bySel.get(i).getThumbnail());
            }
            CateMaxPage cateMaxPage = new CateMaxPage();
            cateMaxPage.setList(bySel);
            cateMaxPage.setMaxPage(bySelCount.getTotalPages());
            cateMaxPage.setCount(bySelCount.getTotalElements());
            return cateMaxPage;
        }

    }

    public List selCateDSL(CateSelList cateSelList) {


        List<CateSelVo1> fetch = jpaQueryFactory.select(Projections.bean(CateSelVo1.class,
                        qProductEntity.productId.as("productId"),
                        qProductThumbnailEntity.img.as("thumbnail"),
                        qProductEntity.pPrice.as("price"),
                        qProductEntity.pName.as("name"),
                        qProductEntity.pQuantity.as("quantity"),
                        qProductEntity.saleVolume.as("saleVoumn"),
                        qProductEntity.pointRate.as("pointRate")))
                .from(qProductEntity)
                .join(qProductCateRelationEntity.productEntity, qProductEntity).on(qProductCateRelationEntity.productEntity.productId.eq(qProductEntity.productId))
                .join(qProductThumbnailEntity.productId, qProductEntity).on(qProductThumbnailEntity.productId.productId.eq(qProductEntity.productId))
                .join(qProductAllergyEntity.productId, qProductEntity).on(qProductAllergyEntity.productId.productId.eq(qProductEntity.productId))
                .where(qProductCateRelationEntity.categoryEntity.cateId.eq(cateSelList.getCateId()).and(qProductEntity.pQuantity.ne(0)).and(qProductEntity.isDelete.eq((byte) 0))
                        .and(qProductThumbnailEntity.img.isNotNull()).and(cateDetail(cateSelList.getCateDetailId())))
                .groupBy(qProductEntity).fetch();

        return fetch;
    }


        private BooleanExpression cateDetail(Long cateDetailId){
        if(cateDetailId==null){
            return null;
        }
        return qProductCateRelationEntity.cateDetailEntity.cateDetailId.eq(cateDetailId);
        }
    }



