package com.green.babymeal.orderbasket;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orderbasket")
@RequiredArgsConstructor
public class OrderBasketController {

    private final OrderBasketService service;
}
