package com.green.babymeal.admin.model;

import com.green.babymeal.common.entity.OrderDetailEntity;
import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.common.entity.ProductEntity;
import com.green.babymeal.common.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class OrderlistRes {
    private Long orderId;
    private Long ordercode;
    private Long iuser;
    private String userName;
    private Byte payment; // 결제수단
    private Byte shipment; // 배송상태
    private Byte cancel; // 취소환불
    private LocalDate createdAt;
    private String phoneNm;
    private String request;
    private String reciever;
    private String address;
    private String addressDetail;
    private Byte delYn;
    private int usepoint;
    private String productName; // 대표상품이름
    private List<OrderDetailVo> orderDetailVo;
    //private List<ProductVo> productVo;
    private UserVo userVo;
}


