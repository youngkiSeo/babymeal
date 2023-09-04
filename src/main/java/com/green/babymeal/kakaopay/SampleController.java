package com.green.babymeal.kakaopay;



import com.green.babymeal.kakaopay.model.KakaoPayDto;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Log
@RestController
@RequestMapping("/api")
public class SampleController {
    @Setter(onMethod_ = @Autowired)
    private KakaoPay kakaopay;


    @GetMapping("/kakaoPay")
    public void kakaoPayGet() {

    }


    @PostMapping("/kakaoPay")
    public String kakaoPay(@RequestBody KakaoPayDto dto) {
        log.info("kakaoPay post............................................");


       return  kakaopay.kakaoPayReady(dto);

    }


    @GetMapping("/kakaoPaySuccess")
    public void kakaoPaySuccess(@RequestParam("pg_token") String pg_token,Model model) {
        log.info("kakaoPaySuccess get............................................");
        log.info("kakaoPaySuccess pg_token : " + pg_token);


        model.addAttribute("info", kakaopay.kakaoPayInfo(pg_token));

    }
}
