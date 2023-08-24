package com.green.babymeal.cate;

import com.green.babymeal.cate.model.CateViewRepositoryCate;
import com.green.babymeal.cate.model.CateViewRepositoryDetail;
import com.green.babymeal.common.entity.CategoryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CateService {

    private final CateRepository cateRepository;
    private final CateDetailRepository cateDetailRepository;
    private final CateViewRepository cateViewRepository;



    public List selCateList(){
        List list1=new ArrayList();
        CategoryEntity entity=new CategoryEntity();
        for (int i = 1; i <= 4; i++) {
            entity.setCateId(Long.valueOf(i));
            CateViewRepositoryCate byCategoryEntity = cateViewRepository.findDistinctByCategoryEntity(entity);
            List<CateViewRepositoryDetail> byCategoryEntity1 = cateViewRepository.findAllByCategoryEntity(entity);
            System.out.println(byCategoryEntity1);
            list1.add(byCategoryEntity);
            list1.add(byCategoryEntity1);
        }
        return list1;
    }
}
