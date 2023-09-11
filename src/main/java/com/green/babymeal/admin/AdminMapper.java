package com.green.babymeal.admin;

import com.green.babymeal.admin.model.*;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {


    // 웹에디터

    int insPk(PkVo pkVo);

    int insWebEditorImg(AdminProductImgDto dto);
    int updAdminProduct(AdminProductUpdDto dto); // 상품 등록
    int insProductCateRelation(AdminProductCateRelationDto dto);
    void delCate(Long productId); // 상품의 카테고리정보 삭제
    int insertAllergyId(@Param("allergyId") List<Long> allergyId, @Param("productId") Long productId); // 상품신규등록-알러지등록
    int updAllergyId(@Param("paramMap") Map<String, Object> paramMap);
    int deleteAllergies(@Param("productId") Long productId);
    //int updateAllergyId(@Param("allergyId") List<Long> allergyId, @Param("productId") Long productId); // 상품수정시 알러지등록


    int changeAdminProduct(AdminProductUpdDto dto); // 상품 수정
    AdminProductUpdDto updProductInfo(int productId); // 상품 수정버튼 클릭시 기존 정보 가져오기
    List<Integer> updProductInfoCate(int productId);
    int delImg(Long productId);
    int delProduct(Long productId);

    int insWebEditorImgList(AdminProductImgDto dto);
    //    int insThumbnail(List<MultipartFile> thumbnail);
//    int updPicTest(CreatePicProduct dto);
//
//    int updPicTestThumb(CreatePicProduct dto);
    int delWebEditorCancel(Long pImgId);
    ProductImgPk selProductImgPk(Long pImgId);

    int insImgList(AdminProductImgDto dto);
    List<AdminProductEntity> getProduct(int productId);
    int delAdminProduct(int productId);

}
