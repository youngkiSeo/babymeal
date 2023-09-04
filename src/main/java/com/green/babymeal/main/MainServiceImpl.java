package com.green.babymeal.main;

import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.ProductCategoryRelationRepository;
import com.green.babymeal.main.model.MainSelPaging;
import com.green.babymeal.main.model.MainSelVo;
import com.green.babymeal.main.model.SelDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class MainServiceImpl implements MainService {

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;
    private final AuthenticationFacade USERPK;
    private final ProductCategoryRelationRepository productCategoryRelationRepository;


    @Override
    public MainSelPaging mainSel(SelDto dto) {

        QProductEntity qProductEntity = new QProductEntity("ProductEntity");
        QProductThumbnailEntity qProductThumbnailEntity = new QProductThumbnailEntity("ProductThumbnailEntity");
        QProductCateRelationEntity qProductCateRelationEntity = new QProductCateRelationEntity("ProductCateRelationEntity");

        int startIdx = (dto.getPage() - 1) * dto.getRow();

        if (dto.getCheck() == 1) {
            //기본으로 보여줄 상품

            List<MainSelVo> fetch = jpaQueryFactory
                    .select(getBean(qProductEntity, qProductThumbnailEntity))
                    .from(qProductEntity)
                    .leftJoin(qProductEntity.productThumbnailEntityList, qProductThumbnailEntity)
                    .on(qProductEntity.productId.eq(qProductThumbnailEntity.productId.productId))
                    .where(qProductEntity.pQuantity.ne(0), qProductEntity.isDelete.eq((byte) 0), qProductThumbnailEntity.img.isNotNull())
                    .orderBy(qProductEntity.createdAt.desc())
                    .offset(startIdx)
                    .limit(dto.getRow())
                    .fetch();

            long count = jpaQueryFactory
                    .select(qProductEntity, qProductThumbnailEntity)
                    .from(qProductEntity)
                    .leftJoin(qProductEntity.productThumbnailEntityList, qProductThumbnailEntity)
                    .on(qProductEntity.productId.eq(qProductThumbnailEntity.productId.productId))
                    .orderBy(qProductEntity.createdAt.desc())
                    .fetchCount();


            productNmCateId(fetch); //상품 이름에 단계를 붙힌다


            return MainSelPaging.builder()
                    .maxPage((int) Math.ceil((double) fetch.size() / dto.getRow()))
                    .maxCount(count)
                    .list(fetch)
                    .build();

        } else if (dto.getCheck() == 2) {
            //회원 자녀의 개월에 따라 상품 추천

            Object userBabyBirth = em.createQuery("select u.birthday from UserEntity u where u.id=:iuser ")
                    .setParameter("iuser", USERPK.getLoginUser().getIuser()).getSingleResult();
            LocalDate userBabyBirthday = (LocalDate) userBabyBirth;
            Period between = Period.between(userBabyBirthday, LocalDate.now());
            System.out.println("between.getMonths() = " + between.getMonths());
            Long cate;
            if (between.getMonths() <= 4) {
                return null;
            } else if (between.getMonths() <= 6) {
                cate = 1L;
            } else if (between.getMonths() <= 10) {
                cate = 2L;
            } else if (between.getMonths() <= 13) {
                cate = 3L;
            } else cate = 4L;

            System.out.println("cate = " + cate);

            List<MainSelVo> fetch = jpaQueryFactory
                    .select(getBean(qProductEntity, qProductThumbnailEntity))
                    .from(qProductEntity)
                    .leftJoin(qProductThumbnailEntity)
                    .on(qProductEntity.productId.eq(qProductThumbnailEntity.productId.productId))
                    .leftJoin(qProductCateRelationEntity)
                    .on(qProductEntity.productId.eq(qProductCateRelationEntity.productEntity.productId))
                    .where(qProductCateRelationEntity.categoryEntity.cateId.eq(cate), qProductEntity.pQuantity.ne(0),
                            qProductEntity.isDelete.eq((byte) 0), qProductThumbnailEntity.img.isNotNull())
                    .orderBy(qProductEntity.saleVolume.desc(), Expressions.numberTemplate(Double.class, "function('rand')").asc())
                    .limit(dto.getRow())
                    .fetch();

            System.out.println("fetch = " + fetch);

            for (MainSelVo vo : fetch) {
                vo.setName("[" + cate + "단계]" + vo.getName());
            }

            return MainSelPaging.builder()
                    .maxCount(Long.valueOf(fetch.size()))
                    .list(fetch)
                    .build();


        } else if (dto.getCheck() == 3) {
            //램덤으로 상품 추천
            List<MainSelVo> fetch = jpaQueryFactory.select(getBean(qProductEntity, qProductThumbnailEntity))
                    .from(qProductEntity)
                    .leftJoin(qProductThumbnailEntity)
                    .on(qProductEntity.productId.eq(qProductThumbnailEntity.productId.productId))
                    .where(qProductEntity.pQuantity.ne(0), qProductEntity.isDelete.eq((byte) 0), qProductThumbnailEntity.img.isNotNull())
                    .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").desc())
                    .limit(dto.getRow())
                    .fetch();

            productNmCateId(fetch); //상품 이름에 단계를 붙힌다

            return MainSelPaging.builder()
                    .maxCount(Long.valueOf(fetch.size()))
                    .list(fetch)
                    .build();


        } else if(dto.getCheck()==4){
            //제일 많이 팔린 상품 추천
            List<MainSelVo> fetch = jpaQueryFactory.select(getBean(qProductEntity, qProductThumbnailEntity))
                    .from(qProductEntity)
                    .leftJoin(qProductThumbnailEntity)
                    .on(qProductEntity.productId.eq(qProductThumbnailEntity.productId.productId))
                    .where(qProductEntity.pQuantity.ne(0), qProductEntity.isDelete.eq((byte) 0), qProductThumbnailEntity.img.isNotNull())
                    .orderBy(qProductEntity.saleVolume.desc(), Expressions.numberTemplate(Double.class, "function('rand')").asc())
                    .limit(dto.getRow())
                    .fetch();

            productNmCateId(fetch);

        return    MainSelPaging.builder()
                    .maxCount(Long.valueOf(fetch.size()))
                    .list(fetch)
                    .build();

        }
        else {
            //제일 많이 팔린 상품 추천 더보기(전체)
            List<MainSelVo> fetch = jpaQueryFactory.select(getBean(qProductEntity, qProductThumbnailEntity))
                    .from(qProductEntity)
                    .leftJoin(qProductThumbnailEntity)
                    .on(qProductEntity.productId.eq(qProductThumbnailEntity.productId.productId))
                    .where(qProductEntity.pQuantity.ne(0), qProductEntity.isDelete.eq((byte) 0), qProductThumbnailEntity.img.isNotNull())
                    .orderBy(qProductEntity.saleVolume.desc())
                    .offset(startIdx)
                    .limit(dto.getRow())
                    .fetch();

            productNmCateId(fetch);

        return    MainSelPaging.builder()
                    .maxPage((int) Math.ceil((double) fetch.size() / dto.getRow()))
                    .maxCount(Long.valueOf(fetch.size()))
                    .list(fetch)
                    .build();

        }
    }



//    //상품에 단계를 붙히는 메소드
//    private void productNmCateId(List<MainSelVo> fetch) {
//        for (MainSelVo vo : fetch) {
//            ProductEntity productEntity = new ProductEntity();
//            productEntity.setProductId(vo.getProductId());
//            ProductCateRelationEntity byProductEntity = productCategoryRelationRepository.findByProductEntity(productEntity);
//            Long productCateId = byProductEntity.getProductCateId();
//            vo.setName("[" + productCateId + "단계]" + vo.getName());
//        }
//    }

    // 1개만 반환하도록 수정하여 추가 - 09-04 // 에러나면 이 코드 삭제해주세요
    // 상품에 단계를 붙히는 메소드

    private void productNmCateId(List<MainSelVo> fetch) {
        for (MainSelVo vo : fetch) {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setProductId(vo.getProductId());
            Optional<ProductCateRelationEntity> relationOptional = productCategoryRelationRepository.findFirstByProductEntity(productEntity);
            if (relationOptional.isPresent()) {
                Long productCateId = relationOptional.get().getProductCateId();
                vo.setName("[" + productCateId + "단계]" + vo.getName());
            }
        }
    }



    private static QBean<MainSelVo> getBean(QProductEntity qProductEntity, QProductThumbnailEntity qProductThumbnailEntity) {
        return Projections.bean(MainSelVo.class,
                qProductEntity.productId,
                qProductThumbnailEntity.img.as("thumbnail"),
                qProductEntity.pName.as("name"),
                qProductEntity.pPrice.as("price"),
                qProductEntity.pQuantity.as("quantity"),
                qProductEntity.saleVolume.as("saleVoumn"),
                qProductEntity.pointRate);
    }
}







