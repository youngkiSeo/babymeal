package com.green.babymeal.mypage;

import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.*;
import com.green.babymeal.mypage.model.OrderlistDetailUserVo;
import com.green.babymeal.mypage.model.OrderlistDetailVo;
import com.green.babymeal.mypage.model.OrderlistSelVo;
import com.green.babymeal.mypage.model.OrderlistUserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final OrderlistRepository orderlistRep;
    private final OrderDetailRepository orderDetailRep;
    private final ThumbnailRepository thumbnailRep;
    private final ProductCategoryRelationRepository productcategoryRep;



    public  List<OrderlistSelVo> orderlist (){
        Long iuser = 1L;
        UserEntity entity = new UserEntity();
        entity.setIuser(1L);
        List<OrderlistEntity> orderlist = orderlistRep.findAllByIuser(entity);

        List<OrderlistSelVo> list = new ArrayList<>();
        for (int i = 0; i <orderlist.size(); i++) {
            List<OrderDetailEntity> OrderDetail = orderDetailRep.findAllByOrderId(orderlist.get(i));
            ProductThumbnailEntity Thumbnail = thumbnailRep.findAllByProductId(OrderDetail.get(0).getProductId());
            ProductCateRelationEntity cate= productcategoryRep.findByProductEntity(OrderDetail.get(0).getProductId());

            int totalPrice =0;
            String name = "";
            for (int j = 0; j <OrderDetail.size(); j++) {
                totalPrice+= OrderDetail.get(i).getTotalPrice();
                Long cateId = cate.getCategoryEntity().getCateId();
                if (OrderDetail.size() > 1){
                    String pName = OrderDetail.get(i).getProductId().getPName();
                    name = "["+cateId+"단계] "+pName + "외 " + (OrderDetail.size()-1) + "개" ;
                }else

                    name="["+cateId+"단계] "+OrderDetail.get(i).getProductId().getPName();
            }

            OrderlistSelVo build = OrderlistSelVo.builder()
                    .orderId(orderlist.get(i).getOrderid())
                    .createdAt(String.valueOf(orderlist.get(i).getCreatedAt()))
                    .price(totalPrice)
                    .name(name)
                    .shipment(orderlist.get(i).getShipment())
                    .thumbnail(Thumbnail.getImg())
                    .build();
            list.add(build);
        }

        return list;
    }

    public OrderlistDetailUserVo orderDetail(Long orderId){
        List<OrderlistDetailVo> byOrderId = orderDetailRep.findByOrderId(orderId);
        OrderlistEntity orderlistEntity = orderlistRep.findByOrderid(orderId);
        OrderlistUserVo vo = new OrderlistUserVo();
        vo.setReciever(orderlistEntity.getReciever());
        vo.setAddress(orderlistEntity.getAddress());
        vo.setAddressDetail(orderlistEntity.getAddressDetail());
        vo.setPhoneNm(orderlistEntity.getPhoneNm());
        vo.setRequest(orderlistEntity.getRequest());
        vo.setUsepoint(orderlistEntity.getUsepoint());
        return OrderlistDetailUserVo.builder().list(byOrderId).user(vo).build();

    }

}
