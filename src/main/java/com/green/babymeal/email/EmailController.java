package com.green.babymeal.email;

import com.green.babymeal.email.model.MailSendDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "메일발송")
@RestController
@EnableScheduling
@RequestMapping("/api/mail")
public class EmailController {

    @Autowired
    private EmailService service;


    @PostMapping("/send")
    @Operation(summary = "메일 발송 기능",description = "사용법 <br>"+
            "mailAddress : 수신자 메일 주소<br>"+
            "title : 메일 제목<br>"+
            "ctnt : 내용 <br>")
    public void postSend(@RequestBody MailSendDto dto) {
        log.info("{}", dto);
        service.send(dto);
        // 보내고 싶은 메일이 있다면 dto 객체에 내용 맞춰서 넣은 후 해당 메소드 호출
    }

    @GetMapping("/password")
    @Operation(summary = "비밀번호 찾기",description =
            "아이디(이메일)과 휴대폰 번호를 입력해주세요.<br>" +
                    "회원정보와 일치 시 등록된 메일 주소로 임시 비밀번호를 발송합니다"
    )
    public String findPassword(@RequestParam String mail, @RequestParam String mobileNb){
        log.info("테스트");
        service.findPassword(mail, mobileNb);
        return service.findPassword(mail, mobileNb);
    }



}
