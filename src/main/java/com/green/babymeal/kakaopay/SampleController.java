package com.green.babymeal.kakaopay;



import com.green.babymeal.kakaopay.model.KakaoPayDto;
import com.green.babymeal.kakaopay.model.QrCodeVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "카카오페이")
@Slf4j
@RestController
@RequestMapping("/api")
public class SampleController {
    @Setter(onMethod_ = @Autowired)
    private KakaoPay kakaopay;


    @GetMapping("/kakaoPay")
    @Operation(summary = "사용x")
    public void kakaoPayGet() {

    }


    @PostMapping("/kakaoPay")
    @Operation(summary = "카카오페이 결제 실행 시 카카오페이 qr코드 주소 반환",description = "" +
            "카카오페이 바로구매 시 해당 productId 와 count 를 보내야한다<br>" +
            "장바구니에서 카카오페이 구매 시 productId와 count를 안 보내도 됨<br>" +
            "address : 주소<br>" +
            "addressDetail : 상세주소<br>" +
            "orderCode : 0으로 보내야 한다<br>" +
            "payment : 기본값 1<br>" +
            "phoneNumber : 유저의 폰 넘버<br>" +
            "reciever : 받는 사람 이름<br>" +
            "request : 요청사항<br>" +
            "shipment : 기본값 1<br>" +
            "usepoint : 결제 시 사용할 포인트<br>")
    public QrCodeVo kakaoPay(@RequestBody KakaoPayDto dto) {
        log.info("kakaoPay post............................................");
        String qrCodePage = kakaopay.kakaoPayReady(dto);
        log.info("qrCode: {}", qrCodePage);
       return QrCodeVo.builder().qrCodePage(qrCodePage).build();

    }


    @GetMapping("/kakaoPaySuccess")
    @Operation(summary = "카카오페이 결제 성공시 accesstoken을 헤더에 박고 pg_token을 쿼리스트링으로 보내야한다")
    public void kakaoPaySuccess(@RequestParam("pg_token") String pg_token,Model model) {
        log.info("kakaoPaySuccess get............................................");
        log.info("kakaoPaySuccess pg_token : " + pg_token);


        model.addAttribute("info", kakaopay.kakaoPayInfo(pg_token));

    }
}
