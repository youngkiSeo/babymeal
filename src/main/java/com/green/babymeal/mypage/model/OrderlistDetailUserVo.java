package com.green.babymeal.mypage.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderlistDetailUserVo {
    List<OrderlistDetailVo> list;
    OrderlistUserVo user;

}
