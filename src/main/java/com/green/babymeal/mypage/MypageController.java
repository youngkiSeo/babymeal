package com.green.babymeal.mypage;

import com.green.babymeal.common.entity.OrderDetailEntity;
import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.mypage.model.*;
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
    OrderlistDetailUserVo getOrderlistDetail(@PathVariable Long orderId){
        return service.orderDetail(orderId);
    }

    @DeleteMapping("/orderlist")
    @Operation(summary = "주문내역삭제",description = ""+
            "orderId: 주문내역PK <br>")
    public OrderlistEntity delorderlist(Long orderId){
        return service.delorder(orderId);
    }

    @GetMapping("/profile")
    @Operation(summary = "내 정보조회",description = ""+
            "image: 프로필이미지<br>"+
            "address: 주소 <br>")
    public ProfileVo getprofile(){
        return service.profile();
    }

    @PatchMapping("/profile")
    @Operation(summary = "내정보(유저 정보) 수정" , description = "")
    ProfileVo patchprofile(@RequestBody ProfileUpdDto dto){
        return service.profileupdate(dto);
    }
}