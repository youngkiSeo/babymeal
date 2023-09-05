package com.green.babymeal.mypage;

import com.green.babymeal.mypage.model.OrderlistSelVo;
import com.green.babymeal.mypage.model.OrderlistStrVo;

import java.util.List;

public interface MypageService {
    List<OrderlistStrVo> orderlist(int month);
}
