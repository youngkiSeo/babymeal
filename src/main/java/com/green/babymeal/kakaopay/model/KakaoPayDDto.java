package com.green.babymeal.kakaopay.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;



@Data
public class KakaoPayDDto {

   private Long iuser;
   private Long orderId;
   private int totalPrice;

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
   private int delYn;

}
