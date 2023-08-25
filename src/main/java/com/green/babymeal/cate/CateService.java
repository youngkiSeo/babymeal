package com.green.babymeal.cate;

import com.green.babymeal.cate.model.*;
import com.green.babymeal.common.entity.CategoryEntity;
import com.green.babymeal.common.repository.CateDetailRepository;
import com.green.babymeal.common.repository.CateRepository;
import com.green.babymeal.common.repository.CateViewRepository;
import com.green.babymeal.common.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CateService {

    private final CateRepository cateRepository;
    private final CateDetailRepository cateDetailRepository;
    private final CateViewRepository cateViewRepository;




    public List selCate(){
        List list1=new ArrayList();
        List<CategoryEntity> all = cateRepository.findAll();
        CategoryEntity entity=new CategoryEntity();
        for (int i = 1; i <= all.size(); i++) {
            entity.setCateId(Long.valueOf(i));
            CateViewRepositoryCate byCategoryEntity = cateViewRepository.findDistinctByCategoryEntity(entity);
            List<CateViewRepositoryDetail> byCategoryEntity1 = cateViewRepository.findAllByCategoryEntity(entity);
            System.out.println(byCategoryEntity1);
            list1.add(byCategoryEntity);
            list1.add(byCategoryEntity1);
        }
        return list1;
    }

    public List<CateSelVo> selCateList(CateSelList cateSelList){
       log.info("{}",cateSelList.getCateDetailId());
        if("null".equals(cateSelList.getCateDetailId())){
          return  cateRepository.findBy(cateSelList.getCateId(),cateSelList.getCateDetailId());
        }
        else{
          return  cateRepository.findBySel(cateSelList.getCateId());
        }



    }
}
