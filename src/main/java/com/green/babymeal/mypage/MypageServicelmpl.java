package com.green.babymeal.mypage;

import ch.qos.logback.core.util.FileUtil;
import com.green.babymeal.common.config.exception.AuthErrorCode;
import com.green.babymeal.common.config.exception.RestApiException;
import com.green.babymeal.common.config.properties.AppProperties;
import com.green.babymeal.common.config.redis.RedisService;
import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.config.security.model.AuthToken;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.*;
import com.green.babymeal.common.utils.MyFileUtils;
import com.green.babymeal.common.utils.MyHeaderUtils;
import com.green.babymeal.mypage.model.*;
import com.green.babymeal.user.UserRepository;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageServicelmpl implements MypageService {

    private final OrderlistRepository orderlistRep;
    private final OrderDetailRepository orderDetailRep;
    private final UserRepository userRep;
    private final PasswordEncoder PW_ENCODER;
    private final AuthenticationFacade USERPK;
    private final ProductCategoryRelationRepository productcaterelationRep;
    private final JPAQueryFactory jpaQueryFactory;
    private final AppProperties appProperties;
    private final RedisService redisService;
    private final BabyAlleRepository babyAlleRep;
    private final BabyRepository babyRep;
    private final MypageMapper mapper;

    QOrderlistEntity orderlist = QOrderlistEntity.orderlistEntity;
    QOrderDetailEntity orderDetail = QOrderDetailEntity.orderDetailEntity;
    QProductThumbnailEntity thumbnail = QProductThumbnailEntity.productThumbnailEntity;

    @Value("${file.dir}")
    private String fileDir;

    @Override
    public List<OrderlistStrVo> orderlist(int month) {

        QProductEntity product = QProductEntity.productEntity;
        UserEntity loginUser = USERPK.getLoginUser();


        Byte delYn = 0;
        int num = 10;
        String pName = null;
        Long productID= 0L;

        List<OrderlistSelVo> order = jpaQueryFactory
                .select(Projections.constructor(OrderlistSelVo.class, orderlist.orderId, orderlist.orderCode, orderlist.createdAt
                        , orderDetail.totalPrice, orderDetail.productId.pName, orderDetail.productId.productId,thumbnail.img, orderlist.shipment))
                .from(orderlist)
                .leftJoin(orderDetail)
                .on(orderDetail.orderId.orderId.eq(orderlist.orderId))
                .leftJoin(thumbnail)
                .on(orderDetail.productId.productId.eq(thumbnail.productId.productId))
                .where(orderlist.iuser.iuser.eq(loginUser.getIuser()), orderlist.delYn.eq(delYn)
                        , yearEq(month))
                .groupBy(orderlist.orderId)
                .fetch();

        for (int i = 0; i < order.size(); i++) {
            Long orderId = order.get(i).getOrderId();

            List<OrderlistDetailVo> orderDetailEntity = orderDetailRep.findByOrderId(orderId);


            int totalprice = 0;
            String fullPath =null;

            Long catenum = 0L;

            for (int j = 0; j < orderDetailEntity.size(); j++) {
                int price = orderDetailEntity.get(j).getTotalPrice();
                totalprice += price;

                 fullPath = orderDetailEntity.get(j).getImg();


                //카테고리 아이디 찾기

                Long productId = orderDetailEntity.get(j).getProductId();
                productID = productId;
                ProductEntity productEntity = ProductEntity.builder().productId(productId).build();
                List<ProductCateRelationEntity> byProductEntity = productcaterelationRep.findByProductEntity_ProductId(productEntity.getProductId());
                Long cateId = byProductEntity.get(0).getCategoryEntity().getCateId();
                catenum = cateId;

                String name = "";
                //이름 찾기
                if (byProductEntity.size() > 1) {
                    name = byProductEntity.get(0).getProductEntity().getPName() + "외" + String.valueOf(byProductEntity.size() - 1) + "개";
                } else
                    name = byProductEntity.get(0).getProductEntity().getPName();


                pName = name;
            }

            String name = "[" + catenum + "단계] " + pName;
            order.get(i).setPName(name);
            order.get(i).setTotalprice(totalprice);
            order.get(i).setImg(fullPath);
            order.get(i).setProductId(productID);



        }
        List<OrderlistStrVo> orderlist = order.stream().map(item -> OrderlistStrVo.builder()
                .orderId(item.getOrderId())
                .orderCode(item.getOrderCode())
                .createdAt(item.getCreatedAt())
                .totalprice(item.getTotalprice())
                .pName(item.getPName())
                .productId(item.getProductId())
                .img(item.getImg())
                .shipment(String.valueOf(item.getShipment()))
                .build()).toList();

        for (int i = 0; i < orderlist.size(); i++) {
            String shipment = orderlist.get(i).getShipment();
            if (shipment.equals("1")) {
                orderlist.get(i).setShipment("배송 준비중");
            } else if (shipment.equals("2")) {
                orderlist.get(i).setShipment("배송 중");
            } else if (shipment.equals("3")) {
                orderlist.get(i).setShipment("주문 취소");
            } else
                orderlist.get(i).setShipment("배송 완료");
        }

        return orderlist;
    }

    private BooleanExpression yearEq(int month) {
        //날짜 계산
        LocalDate today = LocalDate.now();
        LocalDate Month = today.minusMonths(month);

        com.querydsl.core.types.dsl.BooleanExpression booleanExpression = month != 0 ? orderlist.createdAt.between(Month, today) : null;

        return booleanExpression;
    }

    public OrderlistDetailUserVo orderDetail(Long ordercode) {
        OrderlistEntity byOrderCode = orderlistRep.findByOrderCode(ordercode);
        List<OrderlistDetailVo> byOrderId = orderDetailRep.findByOrderId(byOrderCode.getOrderId());
        OrderlistUserVo vo = new OrderlistUserVo();
        vo.setReciever(byOrderCode.getReciever());
        vo.setAddress(byOrderCode.getAddress());
        vo.setAddressDetail(byOrderCode.getAddressDetail());
        vo.setPhoneNm(byOrderCode.getPhoneNm());
        vo.setRequest(byOrderCode.getRequest());
        vo.setUsepoint(byOrderCode.getUsepoint());
        return OrderlistDetailUserVo.builder().list(byOrderId).user(vo).build();

    }

    public OrderlistEntity delorder(Long orderCode) {
        OrderlistEntity entity = orderlistRep.findByOrderCode(orderCode);
        Byte delYn = 1;
        //entity.setOrderid(orderId);
        entity.setDelYn(delYn);
        OrderlistEntity save = orderlistRep.save(entity);
        return save;
    }

    public ProfileVo profile() {
        UserEntity loginUser = USERPK.getLoginUser();
        UserEntity userEntity = userRep.findById(loginUser.getIuser()).get();

        List<UserBabyinfoEntity> babyentity = babyRep.findByUserEntity_Iuser(loginUser.getIuser());

        // 아기 정보 받아오기
        List<BabyVo> vo = new ArrayList<>();
        for (int i = 0; i < babyentity.size(); i++) {

            List<UserBabyalleEntity> babyallergyentity = babyAlleRep.findByUserBabyinfoEntity_BabyId(babyentity.get(i).getBabyId());
            List<Babyallergy> babyDto = new ArrayList<>();
            for (int j = 0; j < babyallergyentity.size(); j++) {
                Babyallergy babyallergy = new Babyallergy();
                String allergyName = babyallergyentity.get(j).getAllergyEntity().getAllergyName();
                Long allergyId = babyallergyentity.get(j).getAllergyEntity().getAllergyId();
                babyallergy.setAllergyId(allergyId);
                babyallergy.setAllergyName(allergyName);
                babyDto.add(babyallergy);
            }
            BabyVo build = BabyVo.builder().babyId(babyentity.get(i).getBabyId())
                    .childBirth(babyentity.get(i).getChildBirth())
                    .allergyname(babyDto).build();
            vo.add(build);
        }

        return ProfileVo.builder()
                .uid(userEntity.getUid())
                .iuser(userEntity.getIuser())
                .address(userEntity.getAddress())
                .addressDetail(userEntity.getAddressDetail())
                .birthday(userEntity.getBirthday())
                .image(userEntity.getImage())
                .mobileNb(userEntity.getMobile_nb())
                .unm(userEntity.getName())
                .nickNm(userEntity.getNickNm())
                .zipcode(userEntity.getZipCode())
                .point(userEntity.getPoint())
                .baby(vo)
                .build();
    }

    public ProfileVo profileupdate(ProfileUpdDto dto) {
        Long iuser = USERPK.getLoginUser().getIuser();
        UserEntity entity = userRep.findById(iuser).get();

        log.info("dtoupw:{}" ,dto.getUpw());
        // "" 이 들어오지 못하도록함
        if (!dto.getNickNm().equals("")) {
            entity.setNickNm(dto.getNickNm());
        }
        if (!dto.getUpw().equals("")) {
            String encode = PW_ENCODER.encode(dto.getUpw());
            entity.setPassword(encode);
        }
        if (!dto.getPhoneNumber().equals("")) {
            entity.setMobile_nb(dto.getPhoneNumber());
        }
        if (!dto.getUpw().equals("")) {
            entity.setName(dto.getUpw());
        }
        if (!dto.getBirthday().equals("")) {
            entity.setBirthday(dto.getBirthday());
        }
        if (!dto.getZipcode().equals("")) {
            entity.setZipCode(dto.getZipcode());
        }
        if (!dto.getAddress().equals("")) {
            entity.setAddress(dto.getAddress());
        }
        if (!dto.getAddressDetail().equals("")) {
            entity.setAddressDetail(dto.getAddressDetail());
        }
        userRep.save(entity);

        return ProfileVo.builder()
                .iuser(entity.getIuser())
                .uid(entity.getUid())
                .address(entity.getAddress())
                .addressDetail(entity.getAddressDetail())
                .birthday(entity.getBirthday())
                .image(entity.getImage())
                .mobileNb(entity.getMobile_nb())
                .unm(entity.getName())
                .nickNm(entity.getNickNm())
                .zipcode(entity.getZipCode())
                .point(entity.getPoint())
                .build();

    }
    public int updPicUser(MultipartFile pic){
        UserEntity loginUser = USERPK.getLoginUser();
        ProfileUpdPicDto dto = new ProfileUpdPicDto();
        dto.setIuser(loginUser.getIuser());
        String centerPath = String.format("%s/user/%d", MyFileUtils.getAbsolutePath(fileDir),loginUser.getIuser());


        File dic = new File(centerPath);
        if(!dic.exists()){
            dic.mkdirs();
        }

        String originFileName = pic.getOriginalFilename();
        String savedFileName = MyFileUtils.makeRandomFileNm(originFileName);
        String savedFilePath = String.format("%s/%s",centerPath, savedFileName);

        File target = new File(savedFilePath);
        try {
            pic.transferTo(target);
        }catch (Exception e) {
            return 0;
        }
        String img = savedFileName;
        dto.setImg(img);
        try {
            int result = mapper.patchProfile(dto);
            if(result == 0) {
                throw new Exception("프로필 사진을 등록할 수 없습니다.");
            }
        } catch (Exception e) {
            //파일 삭제
            target.delete();
            return 0;
        }
        return 1;
    }

    public int nicknmcheck(String nickname) {
        UserEntity loginUser = USERPK.getLoginUser();
        Long iuser = loginUser.getIuser();

        UserEntity NickNm = userRep.findByNickNm(nickname);

        if (!(null == NickNm)) {
            return 1;
        }
        return 0;
    }

    public void deluser(HttpServletRequest req, HttpServletResponse res) {
        String type = appProperties.getAuth().getTokenType();
        String accessToken = resolveToken(req, type);

        if (accessToken != null) {
            AuthToken authToken = new AuthToken(accessToken, appProperties.getAccessTokenKey());

            String blackAccessTokenKey = String.format("%s:%s", appProperties.getAuth().getRedisAccessBlackKey(), accessToken);
            long expiration = authToken.getTokenExpirationTime() - LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            if (expiration > 0) {
                redisService.setValuesWithTimeout(blackAccessTokenKey, "logout", expiration);
            }
        }
        //cookie에서 값 가져오기
        Optional<Cookie> refreshTokenCookie = MyHeaderUtils.getCookie(req, REFRESH_TOKEN);
        if (refreshTokenCookie.isEmpty()) {
            throw new RestApiException(AuthErrorCode.NOT_FOUND_REFRESH_TOKEN);
        }

        Optional<String> refreshToken = refreshTokenCookie.map(Cookie::getValue);
        if (refreshToken.isPresent()) {
            AuthToken authToken = new AuthToken(refreshToken.get(), appProperties.getRefreshTokenKey());
            long iuser = authToken.getUserDetails().getIuser();
            String redisRefreshTokenKey = String.format("%s:%s", appProperties.getAuth().getRedisRefreshKey(), iuser);
            redisService.deleteValues(redisRefreshTokenKey);
        }
        MyHeaderUtils.deleteCookie(req, res, REFRESH_TOKEN);

        UserEntity user = USERPK.getLoginUser();
        Long iuser = user.getIuser();
        UserEntity userentity = userRep.findById(iuser).get();
        userentity.setDelYn((byte) 1);
        userRep.save(userentity);
    }

    public String resolveToken(HttpServletRequest req, String type) {
        String headerAuth = req.getHeader("authorization");
        return headerAuth != null && headerAuth.startsWith(String.format("%s ", type)) ? headerAuth.substring(type.length()).trim() : null;
    }

    public int selpw(String password) {
        UserEntity userId = USERPK.getLoginUser();
        UserEntity userEntity = userRep.findById(userId.getIuser()).get();
        boolean matches = PW_ENCODER.matches(password, userEntity.getPassword());
        if (matches == true) {
            return 1;
        } else return 0;
    }

}
