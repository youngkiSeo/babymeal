package com.green.babymeal.mypage;

import com.green.babymeal.mypage.model.OrderlistSelVo;

import java.util.List;

public interface MypageService {
    List<OrderlistSelVo> orderlist(int month);
}
