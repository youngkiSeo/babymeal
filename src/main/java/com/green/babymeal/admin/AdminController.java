package com.green.babymeal.admin;


import com.green.babymeal.admin.model.OrderlistRes;
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
    @Operation(summary = "주문내역 조회/검색/필터", description = "사용법" +
            "날짜(기간), 검색어, 주문번호, 상품번호, 주문상태 조건으로 검색가능" +
            "필터1 : 검색어  필터2 : 주문번호  필터3 : 상품번호   필터4 : 주문상태" +
            "사용할 검색/필터조건은 1로 보내주세요 ex) filter1 = 1 , 미사용할거라면 X" +
            "날짜 선택 : 조회할 기간의 날짜 YYmmdd - YYmmdd 입력해주세요<br>" +
            "날짜 입력하지 않으면 올해 1월1일~오늘날짜(자정)으로 디폴트값 설정되어 있음" +
            "페이지네이션 예시 : [ { <br>" +
            "  \"page\": 0, <br>" +
            "  \"size\": 10,<br>" +
            "  \"sort\": [<br>" +
            "    \"createdAt,asc <br>" +
            "  ]<br>" +
            "} ] 입니다. > 생성날짜 기준 오름차순 정렬, 내림차순은 desc 입니다 <br>" +
            "변수명, asc/desc(오름차/내림차 택1) 해서 정렬가능<br>")
    public Page<OrderlistRes> allOrder(@RequestParam(required = false) String filter1,
                                       @RequestParam(required = false) String filter2,
                                       @RequestParam(required = false) String filter3,
                                       @RequestParam(required = false) String filter4,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                       Pageable pageable) {

        LocalDateTime startDate;
        LocalDateTime endDate;

        if (start == null) {
            startDate = LocalDate.now().withDayOfYear(1).atStartOfDay(); // 올해 1월 1일부터
        } else {
            startDate = start.atStartOfDay();
        }
        if (end == null) {
            endDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59); // 오늘 자정까지
        } else {
            endDate = end.atTime(23, 59, 59);
        }

        return service.allOrder(startDate, endDate, filter1, filter2, filter3, filter4, pageable);
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

