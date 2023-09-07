package com.green.babymeal.admin;


import com.green.babymeal.admin.model.*;
import com.green.babymeal.common.entity.OrderlistEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
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
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                       Pageable pageable) {
        if (startDate == null) {
            // 올해 1월 1일부터 오늘까지
            LocalDate today = LocalDate.now();
            startDate = LocalDate.of(today.getYear(), Month.JANUARY, 1);
            endDate = today;
        }
        return service.allOrder(startDate, endDate, filter1, filter2, filter3, filter4, pageable);
    }


    @GetMapping("/order/{orderCode}")
    @Operation(summary = "주문상품정보 > 특정 주문번호에서 주문한 상품 전체조회", description = "<br>" +
            "예시 주문번호 : 202308301651 입니다")
    public OrderlistDetailRes selOrder(@PathVariable Long orderCode) {
        return service.selOrder(orderCode);
    }


    @GetMapping("/product")
    @Operation(summary = "상품정보조회(데이터뿌리기) > 관리자용 <br>", description = "<br>" +
            "예시 : { <br>" +
            "  \"page\": 0,<br>" +
            "  \"size\": 100,<br>" +
            "  \"sort\": \"productId\"<br>" +
            "} <br>" )
    public Page<ProductAdminDto> allProduct(Pageable pageable){
        return service.allProduct(pageable);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "특정상품데이터조회 > 관리자용")
    public ProductAdminSelDto selProduct(Long productId){
        return service.selProduct(productId);
    }



    // 웹에디터 -----------------------------
    @PostMapping("/product/webeditor")
    @Operation(summary = "웹에디터 pk가져오는 메소드 상품 등록 할 때 바로 pk를 반환한다")
    public Long webEditorPk(){
        return service.webEditorPk();
    }


    @PostMapping(value = "/{productId}/img",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "웹에디터 이미지 넣기",description = ""+
            "img : 이미지 풀 경로<br>"+
            "pimgId : 웹에디터 이미지의 pk값")
    public ProductImgPkFull insWebEditorImg(@RequestPart MultipartFile img, @PathVariable Long productId){
        return service.insWebEditorImg(img,productId);
    }

    @PostMapping(value = "/{productId}/imglist",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "웹에디터 이미지 리스트로 넣기",description = ""+
            "img : 이미지 풀 경로<br>"+
            "pimgId : 웹에디터 이미지의 pk값")
    public List<ProductImgPkFull> insWebEditorImgList(@RequestPart List<MultipartFile> img, @PathVariable Long productId){
        return service.insWebEditorImgList(img,productId);
    }

    @GetMapping("/webeditor/product/modification")
    @Operation(summary = "상품 수정 버튼 메소드", description = "수정 버튼 클릭 시 기존 상품의 정보를 가져온다<br>"+
            "상품코드(pk) 입력해주세요")
    public AdminProductUpdDto updProductInfo(@RequestParam int productId){
        return service.updProductInfo(productId);
    }

    @PatchMapping("/webeditor/product/modification")
    @Operation(summary = "상품 수정 메소드", description = "수정할 내역 입력 후 클릭하면, 업데이트 됩니다")
    public int updProduct(@RequestBody AdminProductUpdDto dto){
        return service.changeProduct(dto);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "웹에디터에서 상품등록 취소를 하면 테이블에서 이미지 데이터와 빈 값의 상품 테이블 데이터를 삭제")
    public int delProductImg(@PathVariable Long productId){
        return service.delProductImg(productId);
    }

    @DeleteMapping("/{pImgId}/webeditor")
    @Operation(summary = "웹에디터 등록 하기 전 이미지 삭제")
    public int delProductWebImg(@PathVariable Long pImgId){
        return service.delWebEditorCancel(pImgId);
    }

    @PostMapping(value = "/webeditor/product/imglist/thumbnail", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "썸네일 이미지 리스트로 넣기", description = "본문 등록할 때 함께 보내주세요"+
            "img : 이미지<br>"+
            "pimgId : 웹에디터 이미지의 pk값<br>" +
            "productId : 등록할 상품 pk값" )
    public List<ProductImgPkFull> insImgList(@RequestPart List<MultipartFile> img, @RequestParam Long productId){
        return service.insImgList(img,productId);
    }

    @PostMapping("/product")
    @Operation(summary = "최종상품 등록할 때 저장하는 메소드", description = "상품등록 마지막 단계 <br>" +
            "상품 내용이 DB에 등록됩니다" +
            "productId :상품 번호 <br>" +
            "name : 상품 이름 <br>" +
            "price : 상품 가격 <br>" +
            "quantity : 상품 재고량 <br>" +
            "description : 상품 상세 내용 <br>" +
            "saleVolume : 상품 판매량 <br>" +
            "allergy : 상품 알러지 여부 <br>" +
            "category : 1차 카테고리 <br>" +
            "pointRate : 적립률 <br>" +
            "cateDetail : 2차 카테고리 <br>")
    public int insProduct(@RequestBody AdminProductUpdDto dto){
        if(0 < service.updProduct(dto)) return 1;
        else return 0;

    }

    @DeleteMapping ("/product/productId")
    @Operation(summary = "등록된 상품 삭제")
    public int delAdminProduct(@RequestParam int productId){
        return service.delAdminProduct(productId);
    }

    @DeleteMapping("/webeditor/thumbnail")
    @Operation(summary = "썸네일 삭제", description = "썸네일 이름 / 상품pk보내주세요")
    public int deleteThumbnail(@RequestBody AdminThumbnailDelDto dto){
        return service.deleteThumbnail(dto);
    }

}

