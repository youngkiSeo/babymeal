package com.green.babymeal.orderbasket;

import com.green.babymeal.orderbasket.model.OrderBasketCount;
import com.green.babymeal.orderbasket.model.OrderBasketInsDto;
import com.green.babymeal.orderbasket.model.OrderBasketVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderbasket")
@RequiredArgsConstructor
@Tag(name = "장바구니")
public class OrderBasketController {

    private final OrderBasketService service;


    @PostMapping
    @Operation(summary = "장바구니 추가",description = "" +
            "productId : 상품 고유 번호<br>" +
            "count : 구매할 수량")
    public Long post(@RequestBody OrderBasketInsDto dto){
        return service.post(dto);
    }


    @GetMapping
    @Operation(summary = "장바구니 목록",description = "" +
            "cartId : 장바구니 번호<br>" +
            "productId : 상품 번호<br>" +
            "count : 구매할 상품 수량<br>" +
            "price : 상품의 가격<br>" +
            "thumbnail : 상품의 썸네일<br>" +
            "createdAt : 장바구니에 추가한 시간")
    public List<OrderBasketVo> get(){
        return service.get();
    }

    @PutMapping
    @Operation(summary = "장바구니 수량 +,-",description = "" +
            "cartId : 장바구니 번호<br>" +
            "check : 1을 보내면 상품 count +1, 0을 보내면 count -1")
    private int put(@RequestBody OrderBasketCount orderBasketCount){
        return service.put(orderBasketCount);
    }

    @DeleteMapping("/{cartId}")
    @Operation(summary = "장바구니 삭제")
    private int delete(@PathVariable Long cartId){
        return service.delete(cartId);
    }


}
