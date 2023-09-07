package com.green.babymeal.kakaopay;


import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.OrderBasketRepository;
import com.green.babymeal.common.repository.OrderDetailRepository;
import com.green.babymeal.common.repository.OrderlistRepository;
import com.green.babymeal.common.repository.ProductRepository;
import com.green.babymeal.kakaopay.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@Log
@RequiredArgsConstructor
public class KakaoPay {



    private static final String HOST = "https://kapi.kakao.com";

    private KakaoPayReadyVO kakaoPayReadyVO;
    private KakaoPayApprovalVO kakaoPayApprovalVO;
    private final KakaoPayDDto kakaoPayDDto=new KakaoPayDDto();
    private final ProductRepository productRepository;
    private final OrderlistRepository orderlistRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderBasketRepository orderBasketRepository;
    private final AuthenticationFacade USERPK;

    private int check=0;
    private int allTotalPrice=0;
    private int usepoint=0;

    List countList=new LinkedList();
    List totalPriceList=new ArrayList();
    List productIdList=new ArrayList();



    public String kakaoPayReady(KakaoPayDto dto) {

        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "0d385e1ad926e7780e84cdfd46bc0129");
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");


        usepoint=dto.getUsepoint();
        String productName="";
        int totalPrice=0;
        Long code=0L;
        String formattedDate = getDate("yyyyMMdd");
        String randomCode = getRandomCode(4);
        code = Long.valueOf(formattedDate + randomCode);

        kakaoPayDDto.setAddress(dto.getAddress());
        kakaoPayDDto.setAddressDetail(dto.getAddressDetail());
        kakaoPayDDto.setCount(dto.getCount());
        kakaoPayDDto.setPayment(dto.getPayment());
        kakaoPayDDto.setPhoneNumber(dto.getPhoneNumber());
        kakaoPayDDto.setProductId(dto.getProductId());
        kakaoPayDDto.setReciever(dto.getReciever());
        kakaoPayDDto.setRequest(dto.getRequest());
        kakaoPayDDto.setShipment(dto.getShipment());
        kakaoPayDDto.setOrderCode(code);
        kakaoPayDDto.setUsepoint(dto.getUsepoint());



        int productCount=0;
        int countSum=0;

        if(dto.getProductId()!=null || dto.getProductId()!=0){
            check=1;
            ProductEntity productEntity = productRepository.findById(dto.getProductId()).get();
            String pName = productEntity.getPName();
            productName=pName;
            int pPrice = productEntity.getPPrice();
            totalPrice=pPrice*dto.getCount();
            kakaoPayDDto.setTotalPrice(totalPrice);
            productCount=dto.getCount();
            allTotalPrice=totalPrice;
        }



        else {
            check=2;
            List<OrderBasketEntity> byUserEntityIuser = orderBasketRepository.findByUserEntity_Iuser(2L);
            for (int i = 0; i < byUserEntityIuser.size(); i++) {
                OrderBasketEntity orderBasketEntity = byUserEntityIuser.get(i);
                Long productId = orderBasketEntity.getProductEntity().getProductId();
                ProductEntity productEntity = productRepository.findById(productId).get();
                String pName = productEntity.getPName();
                int count = byUserEntityIuser.get(i).getCount();
                countSum+=count;
                productName+=pName+count+"개\n";
                int pPrice = productEntity.getPPrice();
                totalPrice=(pPrice*count);
                countList.add(count);
                totalPriceList.add(pPrice*count);
                productIdList.add(productId);
                allTotalPrice+=totalPrice;
            }

            productCount=countSum;



        }










        // 서버로 요청할 Body
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "1001");
        params.add("partner_user_id",USERPK.getLoginUser().getIuser());
        params.add("item_name", productName); //상품 이름
        params.add("quantity", productCount); //상품의 수량
        params.add("total_amount", allTotalPrice-usepoint); //상품의 총가격
        params.add("tax_free_amount", "100");
        params.add("approved_at", LocalDateTime.now().toString()); //구매일자
        params.add("approval_url", "http://192.168.0.144:5001/kakaopaypayment");
        params.add("cancel_url", "http://192.168.0.144:5001/kakaoPayCancel");
        params.add("fail_url", "http://192.168.0.144:5001/kakaoPaySuccessFail");

        HttpEntity<MultiValueMap<String, Object>> body = new HttpEntity<MultiValueMap<String, Object>>(params, headers);

        try {
            kakaoPayReadyVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaoPayReadyVO.class);

            log.info("" + kakaoPayReadyVO);

            return kakaoPayReadyVO.getNext_redirect_pc_url(); //카카오페이 return

        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "/pay";

    }


    public String getDate(String format) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return currentDate.format(formatter);
    }

    public String getRandomCode(int length) {
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generates a random number between 0 and 9
            codeBuilder.append(digit);
        }

        return codeBuilder.toString();
    }



    @Transactional
    public KakaoPayApprovalVO kakaoPayInfo(String pg_token) {

        log.info("KakaoPayInfoVO............................................");
        log.info("-----------------------------");

        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "0d385e1ad926e7780e84cdfd46bc0129");
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");



        if(check==1){
            OrderlistEntity entity=new OrderlistEntity();
            entity.setAddress(kakaoPayDDto.getAddress());
            entity.setAddressDetail(kakaoPayDDto.getAddressDetail());
            entity.setPayment(kakaoPayDDto.getPayment());
            entity.setReciever(kakaoPayDDto.getReciever());
            entity.setRequest(kakaoPayDDto.getRequest());
            entity.setUsepoint(kakaoPayDDto.getUsepoint());
            entity.setShipment(kakaoPayDDto.getShipment());
            entity.setOrderCode(kakaoPayDDto.getOrderCode());
            entity.setPhoneNm(kakaoPayDDto.getPhoneNumber());
            UserEntity userEntity=new UserEntity();
            userEntity.setIuser(USERPK.getLoginUser().getIuser());        //user pk set하는 곳
            entity.setIuser(userEntity);
            orderlistRepository.save(entity); //오더 리스트 저장



            OrderDetailEntity orderDetail=new OrderDetailEntity();
            orderDetail.setCount(kakaoPayDDto.getCount());
            orderDetail.setOrderId(entity);
            orderDetail.setTotalPrice(kakaoPayDDto.getTotalPrice());
            orderDetail.setDelYn((byte)0);

            ProductEntity productEntity=new ProductEntity();
            productEntity.setProductId(kakaoPayDDto.getProductId());
            orderDetail.setProductId(productEntity);
            orderDetailRepository.save(orderDetail); //오더 디테일 저장
        }

        else {
            OrderlistEntity entity=new OrderlistEntity();
            entity.setAddress(kakaoPayDDto.getAddress());
            entity.setAddressDetail(kakaoPayDDto.getAddressDetail());
            entity.setPayment(kakaoPayDDto.getPayment());
            entity.setReciever(kakaoPayDDto.getReciever());
            entity.setRequest(kakaoPayDDto.getRequest());
            entity.setUsepoint(kakaoPayDDto.getUsepoint());
            entity.setShipment(kakaoPayDDto.getShipment());
            entity.setOrderCode(kakaoPayDDto.getOrderCode());
            entity.setPhoneNm(kakaoPayDDto.getPhoneNumber());
            UserEntity userEntity=new UserEntity();
            userEntity.setIuser(USERPK.getLoginUser().getIuser()); //user PK set 하는곳
            entity.setIuser(userEntity);
            orderlistRepository.save(entity); //오더 리스트 저장

            for (int i = 0; i < countList.size(); i++) {
                OrderDetailEntity orderDetail=new OrderDetailEntity();
                orderDetail.setCount((int)countList.get(i));
                ProductEntity productEntity=new ProductEntity();
                productEntity.setProductId((Long)productIdList.get(i));
                orderDetail.setProductId(productEntity);
                orderDetail.setOrderId(entity);
                orderDetail.setTotalPrice((int)totalPriceList.get(i));
                orderDetail.setDelYn((byte)0);
                orderDetailRepository.save(orderDetail);

            }

        orderBasketRepository.deleteByUserEntity_Iuser(USERPK.getLoginUser().getIuser());

}









        // 서버로 요청할 Body
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", kakaoPayReadyVO.getTid());
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", USERPK.getLoginUser().getIuser());
        params.add("pg_token", pg_token);
        params.add("total_amount", allTotalPrice-usepoint);
        params.add("approved_at", LocalDateTime.now().toString());
        log.info(pg_token);
        HttpEntity<MultiValueMap<String, Object>> body = new HttpEntity<MultiValueMap<String, Object>>(params, headers);


        try {
            kakaoPayApprovalVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaoPayApprovalVO.class);
            log.info("" + kakaoPayApprovalVO);

            return kakaoPayApprovalVO;

        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
