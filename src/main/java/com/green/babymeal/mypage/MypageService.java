package com.green.babymeal.mypage;

import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.common.repository.OrderDetailRepository;
import com.green.babymeal.common.repository.OrderlistRepository;
import com.green.babymeal.mypage.model.OrderlistSelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final OrderlistRepository orderlistRep;
    private final OrderDetailRepository orderdetailRep;


    public  List<OrderlistSelVo> orderlist (){
        Long iuser = 1L;
        UserEntity entity = new UserEntity();
        entity.setIuser(1L);
        List<OrderlistEntity> orderlist = orderlistRep.findAll();
        List<OrderlistEntity> allByIuser = orderlistRep.findAllByIuser(entity);


        List<OrderlistSelVo> list = allByIuser.stream().map(item -> OrderlistSelVo.builder()
                .orderId(item.getOrderid())
                .createdAt(item.getCreatedAt())
                .build()).toList();

        return list;
    }
}
