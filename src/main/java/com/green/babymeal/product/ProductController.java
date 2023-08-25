package com.green.babymeal.product;


import com.green.babymeal.common.entity.ReviewEntity;
import com.green.babymeal.product.model.ProductReviewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "상품")
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping("/review")
    @Operation(summary = "상품 리뷰 작성", description = ""+
            "product_id = 상품코드 <br>" +
            "ctnt = 리뷰내용" )
    int postReview(@RequestBody ProductReviewDto dto){
        return service.postReview(dto);
    }

//    @GetMapping("/review/{productId}")
//    @Operation(summary = "상품번호에 맞는 리뷰 조회", description = ""+
//            "productId = 상품코드 <br>" +
//            "페이지당 10개씩 출력" +
//            "테스트시 참고 : 존재하는 상품번호 예시 1 6")
//    List<ReviewEntity> selReview(@PathVariable Long productId){
//        return service.getReviewById(productId);
//    }
}
