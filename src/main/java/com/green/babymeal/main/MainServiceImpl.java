package com.green.babymeal.main;

import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.ProductEntity;
import com.green.babymeal.common.entity.ProductThumbnailEntity;
import com.green.babymeal.common.entity.QProductEntity;
import com.green.babymeal.common.entity.QProductThumbnailEntity;
import com.green.babymeal.common.repository.ProductRepository;
import com.green.babymeal.main.model.MainSelPaging;
import com.green.babymeal.main.model.MainSelVo;
import com.green.babymeal.main.model.SelDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sun.jdi.IntegerValue;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
@Slf4j
public class MainServiceImpl implements MainService{

    @Autowired
    private EntityManager em;
    @Autowired
    private  JPAQueryFactory jpaQueryFactory;

      @Override
      public MainSelPaging mainSel(SelDto dto) {
          if (dto.getCheck() == 1) {
              QProductEntity qProductEntity = new QProductEntity("ProductEntity");
              QProductThumbnailEntity qProductThumbnailEntity = new QProductThumbnailEntity("ProductThumbnailEntity");
              JPAQueryFactory jpaQueryFactory=new JPAQueryFactory(em);
              List<ProductEntity> fetch = jpaQueryFactory
                      .select(qProductEntity)
                      .from(qProductEntity)
                      .leftJoin(qProductEntity.productThumbnailEntityList,qProductThumbnailEntity)
                      .on(qProductEntity.productId.eq(qProductThumbnailEntity.productId.productId))
                      .orderBy(qProductEntity.createdAt.desc())
                      .fetch();

              Long aLong = jpaQueryFactory
                      .select(qProductEntity.count())
                      .from(qProductEntity)
                      .fetchOne();
              int maxPage = (int) Math.ceil((double) aLong / dto.getRow());

              return MainSelPaging.builder()
                      .maxPage(maxPage)
                      .maxCount(Long.valueOf(aLong).intValue())
                      .entities(fetch)
                      .build();
          }
          return null;
      }
///       if(dto.getCheck()==2){
///
///       }
//       return null;
//}


//    public MainSelPaging mainSel(SelDto dto){
//        if(dto.getCheck()==1){
////            int selMaxPageCount = mapper.selMainCount();
////            int maxPage=(int)Math.ceil((double)selMaxPageCount/dto.getRow());
////            int startIdx=(dto.getPage()-1)*dto.getRow();
////            List<MainSelVo> mainSelVos = mapper.selMainVo(startIdx,dto.getRow());
////            thumbnailNm(mainSelVos);
////
////            MainSelPaging mainSelPaging=MainSelPaging.builder()
////                            .maxPage(maxPage)
////                            .maxCount(selMaxPageCount)
////                            .list(mainSelVos)
////                            .build();
////
////            return mainSelPaging;
//            QProductEntity qProductEntity=new QProductEntity("productEntity");
//            List<ProductEntity> fetch = jpaQueryFactory
//                    .selectFrom(qProductEntity).fetch();
//           return MainSelPaging.builder()
//                    .entities(fetch)
//                    .build();
//
//
//        }
//
//        else if(dto.getCheck()==2){
//
//            int month = mapper.birth(USERPK.getLoginUser().getIuser());
//            log.info("{}:",month);
//            int cate = 0;
//            if (month <= 4) {
//                return null;
//            }
//            if (month > 4 && month <= 6) {
//                cate = 1;
//            } else if (month > 6 && month <= 10) {
//                cate = 2;
//            } else if (month > 10 && month <= 13) {
//                cate = 3;
//            } else if (month > 13) {
//                cate = 4;
//            }
//            log.info("{}:",USERPK.getLoginUser().getIuser());
//            List<MainSelVo> mainSelVos = mapper.birthRecommendFilter(cate, dto.getRow());
//            thumbnailNm(mainSelVos);
//            log.info("{}:",mainSelVos);
//            MainSelPaging mainSelPaging=MainSelPaging.builder()
//                    .list(mainSelVos)
//                    .build();
//
//            return mainSelPaging;
//
//        }
//
//
//        else if(dto.getCheck()==3){
//            List<MainSelVo> mainSelVos = mapper.random();
//            thumbnailNm(mainSelVos);
//            MainSelPaging mainSelPaging= MainSelPaging.builder()
//                    .list(mainSelVos)
//                    .build();
//            return mainSelPaging;
//
//        }
//
//
//       else if(dto.getCheck()==4){
//            List<MainSelVo> mainSelVos = mapper.bestSel();
//            thumbnailNm(mainSelVos);
//            MainSelPaging mainSelPaging=MainSelPaging.builder()
//                    .list(mainSelVos)
//                    .build();
//            return mainSelPaging;
//        }
//
//
//       else {
//            int startIdx=(dto.getPage()-1)*dto.getRow();
//            int maxPageCount = mapper.bestSelAllCount();
//            int maxPage=(int)Math.ceil((double)maxPageCount/dto.getRow());
//            List<MainSelVo> mainSelVos = mapper.bestSelAll(startIdx,dto.getRow());
//            thumbnailNm(mainSelVos);
//            MainSelPaging mainSelPaging=MainSelPaging.builder()
//                    .maxPage(maxPage)
//                    .maxCount(maxPageCount)
//                    .list(mainSelVos)
//                    .build();
//            return mainSelPaging;
//
//        }
//    }
//
//
//
//
//    private void thumbnailNm(List<MainSelVo> mainSelVos) {
//        for (int i = 0; i < mainSelVos.size(); i++) {
//            String thumbnail = mainSelVos.get(i).getThumbnail();
//            Long productId = mainSelVos.get(i).getProductId();
//            String fullPath ="http://192.168.0.144:5001/img/product/"+productId+"/"+thumbnail;
//            mainSelVos.get(i).setThumbnail(fullPath);
//            Long levelSel = mapper.levelSel(mainSelVos.get(i).getProductId());
//            log.info("{}:", mainSelVos.get(i).getProductId());
//            if(levelSel==null){
//                continue;
//            }
//            String name = mainSelVos.get(i).getName();
//            String levelName="["+levelSel+"단계] "+name;
//            mainSelVos.get(i).setName(levelName);
//        }
//    }
}
