package com.green.babymeal.admin;


import com.green.babymeal.common.entity.OrderlistEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Admin")
@Tag(name = "관리자 페이지")
public class AdminController {
    private final AdminService service;


    @GetMapping("/order")
    @Operation(summary = "주문내역 조회", description = "조회할 기간의 날짜 YYmmdd - YYmmdd 입력해주세요<br>" +
            "페이지 예시 : [ { <br>" +
            "  \"page\": 0, <br>" +
            "  \"size\": 10,<br>" +
            "  \"sort\": [<br>" +
            "    \"createdAt,asc <br>" +
            "  ]<br>" +
            "} ] 입니다. > 생성날짜 기준 오름차순 정렬, 내림차순은 desc 입니다 <br>" +
            "변수명, asc/desc(오름차/내림차 택1) 해서 정렬가능<br>")
    public Page<OrderlistEntity> allOrder(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                          Pageable pageable) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return service.allOrder(startDateTime, endDateTime, pageable);
    }


    @GetMapping("/order/{orderCode}")
    @Operation(summary = "주문상품정보 > 특정 주문번호에서 주문한 상품 전체조회", description = "<br>"+
    "예시 주문번호 : 202308301651 입니다")
    public List<OrderlistEntity> selOrder(@PathVariable Long orderCode){
        return service.selOrder(orderCode);
    }
}


//    @GetMapping("/order/search")
//    @Operation(summary = "주문내역 조회 - 검색기능")

