package com.green.babymeal.cate;

import com.green.babymeal.cate.model.*;
import com.green.babymeal.common.entity.CateDetailEntity;
import com.green.babymeal.common.entity.CategoryEntity;
import com.green.babymeal.common.entity.ProductCateRelationEntity;
import com.green.babymeal.common.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ProductCategoryRelationRepository prr;


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

    public List<CateSelVo> selCateList(CateSelList cateSelList) {
        log.info("{}", cateSelList.getCateDetailId());

        List<CateSelVo> by = cateRepository.findBy(cateSelList.getCateId(), cateSelList.getCateDetailId());
        if (null != cateSelList.getCateDetailId()) {
            for (int i = 0; i < by.size(); i++) {
                Long productId = by.get(i).getProductId();
                System.out.println("productId = " + productId);
                ProductCateRelationEntity productCateRelationEntity = prr.findById(productId).get();
                Long productCateId = productCateRelationEntity.getProductCateId();
                System.out.println("productCateId = " + productCateId);
                by.get(i).setName("[" + productCateId + "단계]" + by.get(i).getName());
                by.get(i).setThumbnail("http://192.168.0.144:5001/img/product/" + productId + "/" + by.get(i).getThumbnail());

            }
            return by;

        } else {
            List<CateSelVo> bySel = cateRepository.findBySel(cateSelList.getCateId());
            for (int i = 0; i < bySel.size(); i++) {
                Long productId = bySel.get(i).getProductId();
                ProductCateRelationEntity productCateRelationEntity = prr.findById(productId).get();
                Long productCateId = productCateRelationEntity.getProductCateId();
                bySel.get(i).setName("[" + productCateId + "단계]" + bySel.get(i).getName());
                bySel.get(i).setThumbnail("http://192.168.0.144:5001/img/product/" + productId + "/" + bySel.get(i).getThumbnail());
            }
            return bySel;
        }

    }



}
