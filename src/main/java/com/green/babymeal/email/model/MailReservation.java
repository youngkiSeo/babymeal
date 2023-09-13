package com.green.babymeal.email.model;

import lombok.Data;

import java.util.List;

@Data
public class MailReservation {
    private String title; // 제목
    private List<String> mailAddress; // 수신자 메일주소
    private String ctnt; // 내용
}
