package com.green.babymeal.kakaopay.model;

import lombok.Data;

@Data
public class KakaoPayDto {

    private Long productId;
    private int count;

    private String address;
    private String address_detail;
    private Long orderCode;
    private int payment;
    private String phoneNm;
    private String reciever;
    private String request;
    private int shipment;
    private int usepoint;

    private Long iuser;
    private Long orderId;


}
