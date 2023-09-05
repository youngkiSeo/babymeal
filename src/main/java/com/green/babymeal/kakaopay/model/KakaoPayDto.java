package com.green.babymeal.kakaopay.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KakaoPayDto {

    private Long productId;
    private int count;
    private String address;
    private String addressDetail;
    private Long orderCode;
    private byte payment;
    private String phoneNumber;
    private String reciever;
    private String request;
    private byte shipment;
    private int usepoint;

}
