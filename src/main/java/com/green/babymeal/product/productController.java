package com.green.babymeal.product;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class productController {
}
