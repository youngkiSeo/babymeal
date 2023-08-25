package com.green.babymeal.mypage;

import com.green.babymeal.common.entity.OrderDetailEntity;
import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.common.repository.OrderDetailRepository;
import com.green.babymeal.common.repository.OrderlistRepository;
import com.green.babymeal.mypage.model.OrderlistSelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<OrderlistEntity> orderlist = orderlistRep.findAllByIuser(entity);


        List<OrderlistSelVo> list = new ArrayList<>();
        for (int i = 0; i <orderlist.size(); i++) {
            List<OrderDetailEntity> OrderDetail = orderdetailRep.findAllByOrderId(orderlist.get(i));
            int totalPrice =0;
            String name = "";
            for (int j = 0; j <OrderDetail.size(); j++) {
                totalPrice+= OrderDetail.get(i).getTotalPrice();
                if (OrderDetail.size() > 1){
                    String pName = OrderDetail.get(i).getProductId().getPName();
                    name = pName + "외" + (OrderDetail.size()-1) + "개" ;
                }else
                    name=OrderDetail.get(i).getProductId().getPName();
            }

            OrderlistSelVo build = OrderlistSelVo.builder()
                    .orderId(orderlist.get(i).getOrderid())
                    .createdAt(orderlist.get(i).getCreatedAt())
                    .price(totalPrice)
                    .name(name)
                    .shipment(orderlist.get(i).getShipment())
                    .build();
            list.add(build);
        }

        return list;
    }
}
