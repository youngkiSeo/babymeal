package com.green.babymeal.cate;




import com.green.babymeal.cate.model.CateMaxPage;
import com.green.babymeal.cate.model.CateSelList;
import com.green.babymeal.cate.model.CateSelVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cate")
@Tag(name = "카테고리")
public class CateController {


    private final CateService service;

    @GetMapping("/list")
    @Operation(summary = "카테고리 목록")
    private List selCate(){
     return service.selCate();
  }

    @GetMapping
    @Operation(summary = "해당 카테고리 상품만 조회",description = "" +
            "1차 카테고리 상품만 조회하고 싶으면 cateId만 보낸다<br>" +
            "2차 카테고리 상품을 조회 시 cateId cateDetail 둘다 보낸다<br>" +
            "productId : 상품 번호<br>" +
            "thumbnail : 상품 썸네일<br>" +
            "price : 상품 1개 가격<br>" +
            "name : 상품 이름<br>" +
            "quantity : 상품 재고량<br>" +
            "saleVoumn : 상품 판매량<br>" +
            "pointRate : 적립률")
    private CateMaxPage selCateList(CateSelList cateSelList, Pageable pageable){
        return service.selCateList(cateSelList,pageable);
    }


}
