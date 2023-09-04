package com.green.babymeal.kakaopay;



import com.green.babymeal.buy.BuyService;
import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.ProductEntity;
import com.green.babymeal.common.repository.ProductRepository;
import com.green.babymeal.kakaopay.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;





@Service
@Log
@RequiredArgsConstructor
public class KakaoPay {


    private final KakaoMapper mapper;
    private static final String HOST = "https://kapi.kakao.com";

    private KakaoPayReadyVO kakaoPayReadyVO;
    private KakaoPayApprovalVO kakaoPayApprovalVO;

    private final ProductRepository productRepository;


    private Long productId=0L;
    private KakaoPayDto dto10=null;



    public String kakaoPayReady(KakaoPayDto dto) {

        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "0d385e1ad926e7780e84cdfd46bc0129");
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");


        dto10=dto;

        ProductEntity productEntity=null;
        if(dto.getProductId()!=null){
            productId=dto.getProductId();
            productEntity = productRepository.findById(dto.getProductId()).get();
        }
        else return null;

        //상품 정보를 받아온다

        System.out.println("productEntity.getPName() = " + productEntity.getPName());
        System.out.println("dto = " + dto.getCount());


        // 서버로 요청할 Body
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "1001");
        params.add("partner_user_id",2L);
        params.add("item_name", productEntity.getPName()); //상품 이름
        params.add("quantity", dto.getCount()); //상품의 수량
        params.add("total_amount", productEntity.getPPrice()); //상품의 총가격
        params.add("tax_free_amount", "100");
        params.add("approved_at", LocalDateTime.now().toString()); //구매일자
        params.add("approval_url", "http://localhost:8080/api/kakaoPaySuccess");
        params.add("cancel_url", "http://localhost:8080/api/kakaoPayCancel");
        params.add("fail_url", "http://localhost:8080/api/kakaoPaySuccessFail");

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


    public KakaoPayApprovalVO kakaoPayInfo(String pg_token) {

        log.info("KakaoPayInfoVO............................................");
        log.info("-----------------------------");

        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "0d385e1ad926e7780e84cdfd46bc0129");
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");






//       int idx = 0;
//       Long code = 0L;
//       //바로구매
//       if(productId!=null){
//           String formattedDate = getDate("yyyyMMdd");
//           String randomCode = BuyService.getRandomCode(4);
//           code = Long.valueOf(formattedDate + randomCode);

//           dto10.setOrderCode(code);
//           dto10.setIuser(2L);
//           mapper.insOrderList(dto10);
//       }







        // 서버로 요청할 Body
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", kakaoPayReadyVO.getTid());
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", 2L);
        params.add("pg_token", pg_token);
        params.add("total_amount", "2100");
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
