package com.green.babymeal.mypage;

import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.*;
import com.green.babymeal.mypage.model.*;
import com.green.babymeal.user.UserRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageServicelmpl implements MypageService{

    private final OrderlistRepository orderlistRep;
    private final OrderDetailRepository orderDetailRep;
    private final UserRepository userRep;
    private final PasswordEncoder PW_ENCODER;
    private final AuthenticationFacade USERPK;
    private final ThumbnailRepository thumbnailRep;
    private final ProductCategoryRelationRepository productcategoryRep;

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;



    @Override
    public  List<OrderlistSelVo> orderlist (){

        QOrderlistEntity orderlist = QOrderlistEntity.orderlistEntity;
        QOrderDetailEntity orderDetail = QOrderDetailEntity.orderDetailEntity;
        QProductEntity product = QProductEntity.productEntity;
        QProductThumbnailEntity thumbnail = QProductThumbnailEntity.productThumbnailEntity;
        UserEntity loginUser = USERPK.getLoginUser();



        List<OrderlistSelVo> order = jpaQueryFactory
                .select(Projections.constructor(OrderlistSelVo.class, orderlist.orderId,orderlist.orderCode,orderlist.createdAt
                        ,orderDetail.totalPrice))
                .from(orderlist)
                .leftJoin(orderDetail)
                .on(orderDetail.orderId.orderId.eq(orderlist.orderId))
                .where(orderlist.iuser.iuser.eq(loginUser.getIuser()))
                .fetch();


        return order;
    }

    public OrderlistDetailUserVo orderDetail(Long orderId){
        List<OrderlistDetailVo> byOrderId = orderDetailRep.findByOrderId(orderId);
        OrderlistEntity orderlistEntity = orderlistRep.findByOrderId(orderId);
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


        LocalDate start = LocalDate.parse("2020-01-01");
        LocalDate end = LocalDate.parse("2023-08-31");
        List<SaleVolumnVo> saleVolumnVos = saleRep.find();
        //List<SaleVolumnVo> allByCreatedAtBetween = saleRep.findAllByCreatedAtBetween(start, end);

        for (int i = 0; i <saleVolumnVos.size(); i++) {
            int price = saleVolumnVos.get(i).getPrice();
            Long count = saleVolumnVos.get(i).getCount();
            long result = price * count;
            saleVolumnVos.get(i).setPrice((int) result);
        }

        return saleVolumnVos;

    }


}
