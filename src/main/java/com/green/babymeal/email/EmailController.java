package com.green.babymeal.email;

import com.green.babymeal.email.model.MailSendDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "메일발송, 현재 사용 XXXX")
@RestController
@EnableScheduling
@RequestMapping("/api/mail")
public class EmailController {

    //1차 당시 사용했던 메일 발송 및 비밀번호 찾기 소스코드입니다
    //현재 common config mail패키지 사용
    //이 코드의 동작을 원할 경우 common config mail패키지 및 maven 주석처리 필요
    private EmailService service; // 인스턴스 필드로 유지

    @Autowired
    public EmailController(EmailService service) {
        this.service = service;
    }

    @PostMapping("/send")
    @Operation(summary = "메일 발송 기능",description = "사용법 <br>"+
            "mailAddress : 수신자 메일 주소<br>"+
            "title : 메일 제목<br>"+
            "ctnt : 내용 <br>")
    public String postSend(@RequestBody MailSendDto dto) {
        log.info("{}", dto);
        return service.send(dto);
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