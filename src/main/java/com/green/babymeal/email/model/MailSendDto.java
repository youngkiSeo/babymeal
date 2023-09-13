package com.green.babymeal.email.model;

import lombok.Data;

@Data
public class MailSendDto {
    private String title; // 제목
    private String mailAddress; // 수신자 메일주소
    private String ctnt; // 내용
}
