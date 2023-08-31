package com.green.babymeal.mypage;

import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.*;
import com.green.babymeal.mypage.model.*;
import com.green.babymeal.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {
    @Autowired
    private final OrderlistRepository orderlistRep;
    @Autowired
    private final OrderDetailRepository orderDetailRep;
    @Autowired
    private final UserRepository userRep;
    @Autowired
    private final PasswordEncoder PW_ENCODER;
    @Autowired
    private final AuthenticationFacade USERPK;
    @Autowired
    private final ThumbnailRepository thumbnailRep;
    @Autowired
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
        UserEntity entity = userRep.findById(loginUser.getIuser()).get();

        if (!dto.getNickNm().equals("")){
            entity.setNickNm(dto.getNickNm());
        }

        if (!dto.getPassword().equals("")){
            String encode = PW_ENCODER.encode(dto.getPassword());
            entity.setPassword(encode);
        }
        if (!dto.getPhoneNumber().equals("")) {
            entity.setMobile_nb(dto.getPhoneNumber());
        }
        if (!dto.getName().equals("")) {
            entity.setName(dto.getName());
        }
        if (!dto.getBirthday().equals("")){
            entity.setBirthday(dto.getBirthday());
        }
        if (!dto.getZipcode().equals("")){
            entity.setZipCode(dto.getZipcode());
        }
        if (!dto.getAddress().equals("")){
            entity.setAddress(dto.getAddress());
        }
        if (!dto.getAddressDetail().equals("")){
            entity.setAddressDetail(dto.getAddressDetail());
        }
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
    public int nicknmcheck(String nickname){
        UserEntity loginUser = USERPK.getLoginUser();
        Long iuser = loginUser.getIuser();
        UserEntity userentity = userRep.findById(iuser).get();

        UserEntity NickNm = userRep.findByNickNm(nickname);

        if (!(null==NickNm)) {
            return 1;
        }
        return 0;
    }
    public void deluser(HttpServletRequest req) {
        UserEntity user = USERPK.getLoginUser();
        Long iuser = user.getIuser();
        UserEntity userentity = userRep.findById(iuser).get();
        log.info("user: {}",userentity.getName());
        userentity.setDelYn((byte) 1);
        userRep.save(userentity);
    }

    public int selpw(String password){
        UserEntity userId = USERPK.getLoginUser();
        UserEntity userEntity = userRep.findById(userId.getIuser()).get();
        boolean matches = PW_ENCODER.matches(password, userEntity.getPassword());
        if (matches==true){
            return 1;
        }else return 0;
    }


    private final ProductRepository productRep;
    private final SaleVolumnRepository saleRep;
    public SaleVolumnEntity Inssalevolumn(SaleVolumnDto dto){
        ProductEntity productEntity = productRep.findById(dto.getProductId()).get();
        SaleVolumnEntity entity = SaleVolumnEntity.builder().count(dto.getCount()).productId(productEntity).build();

        SaleVolumnEntity save = saleRep.save(entity);

        return save;
    }
    public List<SaleVolumnVo> Selectsale(){

        List<SaleVolumnVo> saleVolumnVos = saleRep.find();
        //   List<SaleVolumnVo> allByCreatedAtBetween = saleRep.findAllByCreatedAtBetween(start, end);

        return saleVolumnVos;

    }


}
