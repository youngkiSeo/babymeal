package com.green.babymeal.product;


import com.green.babymeal.product.model.ProductReviewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/review/ins")
    @Operation(summary = "상품 리뷰 작성", description = ""+
            "product_id = 상품코드 <br>" +
            "ctnt = 리뷰내용" )
    int postReview(@RequestBody ProductReviewDto dto){
        return service.postReview(dto);
    }
}
