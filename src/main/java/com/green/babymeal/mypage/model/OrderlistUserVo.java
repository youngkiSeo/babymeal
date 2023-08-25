package com.green.babymeal.mypage.model;

import lombok.Data;

@Data
public class OrderlistUserVo {
    private String reciever;
    private String address;
    private String addressDetail;
    private String phoneNm;
    private String request;
    private int usepoint;
}
