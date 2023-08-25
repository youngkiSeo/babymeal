package com.green.babymeal.mypage;

import com.green.babymeal.common.entity.OrderDetailEntity;
import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.mypage.model.OrderlistDetailUserVo;
import com.green.babymeal.mypage.model.OrderlistDetailVo;
import com.green.babymeal.mypage.model.OrderlistSelVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "마이페이지")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {
    private final MypageService service;

    @GetMapping("/orderlist")
    @Operation(summary = "주문내역조회",description = ""+
            "month: 조회하고싶은 기간(개월) <br>")
    public List<OrderlistSelVo> orderlist(){
       return service.orderlist();
    }

    @GetMapping("/orderlist/{orderId}")
    @Operation(summary = "상세 주문내역",description = "")
    List<OrderlistDetailVo> getOrderlistDetail(@PathVariable Long orderId){
        return service.orderDetail(orderId);
    }
}