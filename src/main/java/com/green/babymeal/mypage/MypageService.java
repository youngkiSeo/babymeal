package com.green.babymeal.mypage;

import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.*;
import com.green.babymeal.mypage.model.*;
import com.green.babymeal.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {
    private final OrderlistRepository orderlistRep;
    private final OrderDetailRepository orderDetailRep;
    private final UserRepository userRep;
    private final PasswordEncoder PW_ENCODER;
    private final AuthenticationFacade USERPK;
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
    public OrderlistEntity delorder(Long orderId){
        OrderlistEntity entity = orderlistRep.getReferenceById(orderId);
        Byte delYn = 1;
        //entity.setOrderid(orderId);
        entity.setDelYn(delYn);
        OrderlistEntity save = orderlistRep.save(entity);
        return save;
    }
    public ProfileVo profile(){
        UserEntity loginUser = USERPK.getLoginUser();
        Optional<UserEntity> byId = userRep.findById(loginUser.getIuser());
        return ProfileVo.builder()
                .iuser(byId.get().getIuser())
                .address(byId.get().getAddress())
                .addressDetail(byId.get().getAddressDetail())
                .birthday(byId.get().getBirthday())
                .email(byId.get().getEmail())
                .image(byId.get().getImage())
                .mobileNb(byId.get().getMobile_nb())
                .name(byId.get().getName())
                .nickNm(byId.get().getNickNm())
                .zipcode(byId.get().getZipCode())
                .point(byId.get().getPoint())
                .build();
    }
    public ProfileVo profileupdate(ProfileUpdDto dto){
        UserEntity loginUser = USERPK.getLoginUser();
        Optional<UserEntity> byid = userRep.findById(loginUser.getIuser());

        UserEntity entity = new UserEntity();
        entity.setIuser(loginUser.getIuser());
        entity.setNickNm(dto.getNickNm());

        log.info("userId:{}",loginUser.getUid());
        entity.setNickNm(dto.getNickNm());
        String encode = PW_ENCODER.encode(dto.getPassword());
        entity.setPassword(encode);
        entity.setMobile_nb(dto.getPhoneNumber());
        entity.setName(dto.getName());
        entity.setBirthday(dto.getBirthday());
        entity.setZipCode(dto.getZipcode());
        entity.setAddress(dto.getAddress());
        entity.setAddressDetail(dto.getAddressDetail());
        entity.setUid(byid.get().getUid());
        entity.setEmail(byid.get().getEmail());

//        if (!dto.getNickNm().equals("")){
//            String nickNm = byid.get().getNickNm();
//            entity.setNickNm(nickNm);
//        }
//
//        if (!dto.getPassword().equals("")){
//        }
//
//        if (!dto.getPhoneNumber().equals("")) {
//        }
//
//        if (!dto.getName().equals("")) {
//        }
//        if (!dto.getBirthday().equals("")){
//        }
//        if (!dto.getZipcode().equals("")){
//
//        }
//        if (!dto.getAddress().equals("")){
//
//        }
//        if (!dto.getAddressDetail().equals("")){
//
//        }


        userRep.save(entity);

        return ProfileVo.builder()
                .iuser(entity.getIuser())
                .address(entity.getAddress())
                .addressDetail(entity.getAddressDetail())
                .birthday(entity.getBirthday())
                .email(entity.getEmail())
                .image(entity.getImage())
                .mobileNb(entity.getMobile_nb())
                .name(entity.getName())
                .nickNm(entity.getNickNm())
                .zipcode(entity.getZipCode())
                .point(entity.getPoint())
                .build();

    }


}
