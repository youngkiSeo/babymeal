package com.green.babymeal.buy;

import com.green.babymeal.buy.model.BuyInsDto;
import com.green.babymeal.buy.model.BuyProductVo;
import com.green.babymeal.buy.model.BuySelVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Buy")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/buy")
public class BuyController {

    @Autowired
    private final BuyService service;

    @PostMapping("/order")
    @Operation(summary = "상품 구매",description = "사용법 <br>"+
            "cartId : 장바구니 PK 번호<br>"+
            "productId : 상품 PK 번호<br>"+
            "count : 상품 갯수 <br>"+
            "totalPrice : 주문한 상품의 가격 <br>"+
            "point : 포인트 사용 금액 <br>"+
            "payment : 결제방법 <br>"+
            "무통장입금(0)/카드(1)/계좌이체(2) <br>"+
            "request: 요청사항 <br>"+
            "receiver: 수령인 <br>"+
            "totalprice: 상품들의 총 가격"+
            "point: 포인트 사용금액<br>"+
            "paymentprice: 결제한 금액<br>"+
            "orderId: 주문번호 PK<br>"
    )
    public BuyProductVo BuyProduct(@RequestBody BuyInsDto dto){
        return service.BuyProduct(dto);
    }

    @GetMapping("/product")
    @Operation(summary = "상품 보여주기")
    public BuySelVo getProduct(@RequestParam Long productId, @RequestParam int count){
        BuySelVo buySelVo = service.selProduct(productId, count);
        return buySelVo;
    }
}
