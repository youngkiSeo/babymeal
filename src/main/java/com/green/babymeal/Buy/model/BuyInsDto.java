package com.green.babymeal.Buy.model;

import lombok.Data;

import java.util.List;
@Data
public class BuyInsDto {
    private String receiver;
    private String address;
    private String addressDetail;
    private String phoneNm;
    private String request;
    private int payment;
    private int point;

    List<BuyInsOrderbasketDto> Insorderbasket;
}
