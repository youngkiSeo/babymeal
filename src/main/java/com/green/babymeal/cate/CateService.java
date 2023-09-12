package com.green.babymeal.cate;

import com.green.babymeal.cate.model.*;
import com.green.babymeal.common.entity.CateDetailEntity;
import com.green.babymeal.common.entity.CategoryEntity;
import com.green.babymeal.common.entity.ProductCateRelationEntity;
import com.green.babymeal.common.repository.*;
import io.netty.util.internal.UnstableApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CateService {

    private final CateRepository cateRepository;
    private final CateDetailRepository cateDetailRepository;
    private final CateViewRepository cateViewRepository;
    private final ProductCategoryRelationRepository productCategoryRelationRepository;


    public List selCate() {
        List<CategoryEntity> all = cateRepository.findAll();
        List list=new ArrayList();

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

        List<CateSelVo> by = cateRepository.findBy(cateSelList.getCateId(), cateSelList.getCateDetailId(),pageable);
        Page<List<CateSelVo>> byCount = cateRepository.findByCount(cateSelList.getCateId(), cateSelList.getCateDetailId(), pageable);


        if (null != cateSelList.getCateDetailId()) {
            for (int i = 0; i < by.size(); i++) {
                ProductCateRelationEntity productCateRelationEntities = productCategoryRelationRepository.find(by.get(i).getProductId());

                by.get(i).setName("[" + productCateRelationEntities.getCategoryEntity().getCateId() + "단계]" + by.get(i).getName());
                by.get(i).setThumbnail(/*"/img/product/" + by.get(i).getProductId() + "/" + */by.get(i).getThumbnail());
            }
           CateMaxPage cateMaxPage=new CateMaxPage();
           cateMaxPage.setList(by);
           cateMaxPage.setMaxPage(byCount.getTotalPages());
           cateMaxPage.setCount(byCount.getTotalElements());
            return cateMaxPage;

        } else {
            List<CateSelVo> bySel = cateRepository.findBySel(cateSelList.getCateId(),pageable);
            Page<List<CateSelVo>> bySelCount = cateRepository.findBySelCount(cateSelList.getCateId(), pageable);

            for (int i = 0; i < bySel.size(); i++) {
                ProductCateRelationEntity productCateRelationEntities = productCategoryRelationRepository.find(bySel.get(i).getProductId());


                bySel.get(i).setName("[" + productCateRelationEntities.getCategoryEntity().getCateId() + "단계]" + bySel.get(i).getName());
                bySel.get(i).setThumbnail(/*"/img/product/" + bySel.get(i).getProductId() + "/" + */bySel.get(i).getThumbnail());
            }
            CateMaxPage cateMaxPage=new CateMaxPage();
            cateMaxPage.setList(bySel);
            cateMaxPage.setMaxPage(bySelCount.getTotalPages());
            cateMaxPage.setCount(bySelCount.getTotalElements());
            return cateMaxPage;
            }

        }

    }


