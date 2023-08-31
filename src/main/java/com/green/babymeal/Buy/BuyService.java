package com.green.babymeal.Buy;

import com.green.babymeal.Buy.model.BuyInsDto;
import com.green.babymeal.Buy.model.BuyProductVo;
import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.*;
import com.green.babymeal.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;


@Slf4j
@Service
@RequiredArgsConstructor
public class BuyService {

    private final AuthenticationFacade USERPK;
    @Autowired
    private final OrderBasketRepository orderBasketRep;
    @Autowired
    private final OrderlistRepository orderlistRep;
    @Autowired
    private final OrderDetailRepository orderDetailRep;
    @Autowired
    private final SaleVolumnRepository saleVolumnRep;
    @Autowired
    private final ProductRepository productRep;
    @Autowired
    private final UserRepository userRep;



    public BuyProductVo BuyProduct(BuyInsDto dto){
        UserEntity loginUser = USERPK.getLoginUser();
        Long iuser = loginUser.getIuser();
        UserEntity userEntity = userRep.findById(iuser).get();

        final Byte delYn = 0;
        final Byte shipment = 1;


        //랜덤코드만들기
        int idx = 0;
        Long code = 0L;
        while (idx<1) {
            String formattedDate = getDate("yyyyMMdd");
            String randomCode = getRandomCode(4);
            code = Long.valueOf(formattedDate + randomCode);
            OrderlistEntity byOrdercode = orderlistRep.findByOrderCode(code);

            if (byOrdercode==null){
                idx++;
            }
        }
        log.info("code:{}",code);
        OrderlistEntity orderlistEntity = OrderlistEntity.builder().orderCode(code).iuser(userEntity).payment(dto.getPayment()).phoneNm(dto.getPhoneNm()).shipment(shipment)
                .request(dto.getRequest()).reciever(dto.getReceiver()).address(dto.getAddress()).addressDetail(dto.getAddressDetail()).usepoint(dto.getPoint()).build();

        orderlistRep.save(orderlistEntity);

        int totalprice = 0;
        int point = 0;
        for (int i = 0; i <dto.getInsorderbasket().size(); i++) {

            // pointrate 가져오기
            Long productId = dto.getInsorderbasket().get(i).getProductId();
            ProductEntity productEntity = productRep.findById(productId).get();
            int pQuantity = productEntity.getPQuantity();
            int saleVolume = productEntity.getSaleVolume();
            productEntity.setPQuantity(pQuantity-dto.getInsorderbasket().get(i).getCount());
            productEntity.setSaleVolume(saleVolume+dto.getInsorderbasket().get(i).getCount());

            float pointRate = productEntity.getPointRate();
            log.info("상품 적립률:{}", pointRate);

            totalprice+=dto.getInsorderbasket().get(i).getTotalprice();
            //포인트 적립
            point += dto.getInsorderbasket().get(i).getTotalprice() * pointRate;

            //orderdetail table insert
            OrderDetailEntity orderDetail = OrderDetailEntity.builder().orderId(orderlistEntity).productId(productEntity).count(dto.getInsorderbasket().get(i).getCount()).delYn(delYn).totalPrice(dto.getInsorderbasket().get(i).getTotalprice()).build();
            orderDetailRep.save(orderDetail);

            SaleVolumnEntity saleVolumnEntity = SaleVolumnEntity.builder().productId(productEntity).count(dto.getInsorderbasket().get(i).getCount()).build();
            saleVolumnRep.save(saleVolumnEntity);

            //product 상품수량 업데이트
            productRep.save(productEntity);

            // 장바구니 삭제
            orderBasketRep.deleteById(dto.getInsorderbasket().get(i).getCartId());

        }
        //유저 point 업데이트
        int resultpoint = dto.getPoint() - point;
        userEntity.setPoint(userEntity.getPoint()-resultpoint);
        userRep.save(userEntity);

        int paymentprice = totalprice- dto.getPoint();

        BuyProductVo build = BuyProductVo.builder().OrderId(orderlistEntity.getOrderId())
                .OrderCode(orderlistEntity.getOrderCode())
                .totalprice(totalprice)
                .point(dto.getPoint())
                .paymentprice(paymentprice).build();


        return build;
    }

    public static String getDate(String format) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return currentDate.format(formatter);
    }

    public static String getRandomCode(int length) {
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generates a random number between 0 and 9
            codeBuilder.append(digit);
        }

        return codeBuilder.toString();
    }

}
