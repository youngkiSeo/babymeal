package com.green.babymeal.admin.model;

import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.mypage.model.OrderlistDetailVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
public class OrderlistDetailRes {
    private Long orderDetailId;
    private Long productId;
    private String productName;
    private int count;
    private int totalPrice;
    private List<OrderlistDetailVo> orderDetailVo;
//    private List<OrderlistEntity> orderlist;
    //private UserVo userVo;
}
