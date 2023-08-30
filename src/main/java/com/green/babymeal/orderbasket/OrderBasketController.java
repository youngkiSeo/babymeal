package com.green.babymeal.orderbasket;

import com.green.babymeal.orderbasket.model.OrderBasketInsDto;
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
    public Long post(@RequestBody OrderBasketInsDto dto){
        return service.post(dto);
    }


    @GetMapping
    public List get(){
        return service.get();
    }
}
