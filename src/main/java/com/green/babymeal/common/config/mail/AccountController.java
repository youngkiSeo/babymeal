package com.green.babymeal.common.config.mail;

import com.green.babymeal.common.config.mail.model.MailDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Tag(name = "이메일 인증, 비밀번호 찾기")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login")
public class AccountController {
    private final EmailService emailService;

    @PostMapping("/mailConfirm")
    @ResponseBody
    @Operation(summary = "이메일로 인증코드 발송, 네이버 메일만 가능", description = "발송 성공 시 Response body에 인증코드뜸")
    public String mailConfirm(@RequestParam String email) throws Exception {
        String code = emailService.sendSimpleMessage(email);
        log.info("인증코드 : " + code);
        return code;
    }

    @PostMapping("/codeConfirm")
    @ResponseBody
    @Operation(summary = "이메일 인증번호 확인", description = "인증번호 일치 시 1, 불일치 시 0 리턴, 2분간만 유효")
    public String codeConfirm(@RequestParam String key) throws Exception{
        return emailService.verifyEmail(key);
    }
}
