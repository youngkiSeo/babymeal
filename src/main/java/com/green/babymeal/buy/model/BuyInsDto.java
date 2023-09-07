package com.green.babymeal.buy.model;

import lombok.Data;

import java.util.List;
@Data
public class BuyInsDto {
    private String receiver;
    private String address;
    private String addressDetail;
    private String phoneNm;
    private String request;
    //private Byte payment;
    private int point;

    List<BuyInsOrderbasketDto> Insorderbasket;
}
