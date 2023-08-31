package com.green.babymeal.Buy;

import com.green.babymeal.Buy.model.BuyInsDto;
import com.green.babymeal.Buy.model.BuyProductVo;
import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.common.repository.OrderBasketRepository;
import com.green.babymeal.common.repository.OrderDetailRepository;
import com.green.babymeal.common.repository.OrderlistRepository;
import com.green.babymeal.common.repository.SaleVolumnRepository;
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



    public BuyProductVo BuyProduct(BuyInsDto dto){
        UserEntity loginUser = USERPK.getLoginUser();
        Long iuser = loginUser.getIuser();
        final Byte payment = 1;
        final Byte shipment = 1;


        //랜덤코드만들기
        int idx = 0;
        Long code = 0L;
        while (idx<1) {
            String formattedDate = getDate("yyyyMMdd");
            String randomCode = getRandomCode(4);
            code = Long.valueOf(formattedDate + randomCode);
            OrderlistEntity byOrdercode = orderlistRep.findByOrdercode(code);

            if (byOrdercode==null){
                idx++;
            }
        }
        log.info("code:{}",code);
//        UserEntity userEntity = UserEntity.builder().iuser(loginUser.getIuser()).build();
//        OrderlistEntity orderlistEntity = OrderlistEntity.builder().ordercode(code).iuser(userEntity)
//                .phoneNm(dto.getPhoneNm()).request(dto.getRequest()).



        return null;
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
