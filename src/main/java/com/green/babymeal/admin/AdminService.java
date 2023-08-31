package com.green.babymeal.admin;

import com.green.babymeal.admin.model.OrderlistRes;
import com.green.babymeal.common.entity.OrderDetailEntity;
import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.common.repository.OrderDetailRepository;
import com.green.babymeal.common.repository.OrderlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private OrderlistRepository orderlistRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;


    public Page<OrderlistRes> allOrder(LocalDateTime startDate, LocalDateTime endDate,
                                       String filter1, String filter2, String filter3, String filter4,
                                       Pageable pageable) {
        // "필터1 : 검색어  필터2 : 주문번호  필터3 : 상품번호   필터4 : 주문상태"
        //기간 내 주문리스트를 가져온다
        Page<OrderlistEntity> outputOrderlist = orderlistRepository.findByCreatedAtBetween(startDate, endDate, pageable);
        // 주문정보 담을 객체 생성
        List<OrderlistRes> resultList = new ArrayList<>();

        //주문번호를 기반으로 주문상세정보를 가져와 조회
        for (OrderlistEntity order : outputOrderlist.getContent()) {
            List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderId_Ordercode(order.getOrdercode());
            // 주문 상세정보가 있으면 데이터 가져옴
            if (!orderDetails.isEmpty()) {
                OrderlistRes orderlistRes = new OrderlistRes();
                orderlistRes.setOrderlist(order);
                orderlistRes.setOrderDetails(orderDetails);
                resultList.add(orderlistRes);
            }
        }

        // 필터2 : 주문번호 기준 필터링
        if (filter2 != null) {
            resultList.removeIf(orderRes -> !orderRes.getOrderlist().getOrdercode().equals(Long.parseLong(filter2)));
        }

        if (filter3 != null) {
            resultList.removeIf(orderRes -> {
                for (OrderDetailEntity orderDetail : orderRes.getOrderDetails()) {
                    if (!orderDetail.getProductId().getProductId().equals(Long.parseLong(filter3))) {
                        return true; // 상품번호가 일치하지 않으면 해당 주문 정보를 리스트에서 제거
                    }
                }
                return false;
            });
        }

        if (filter4 != null) {
            resultList.removeIf(orderRes -> !orderRes.getOrderlist().getPayment().equals(Long.parseLong(filter4)));
        }

        // Page 객체로 변환
        Page<OrderlistRes> resultPage = new PageImpl<>(resultList, pageable, resultList.size());

        return resultPage;
    }


//        if (filter1 != null) {
//            //검색어
//            filterOrderlist = applyFilter1(filterOrderlist, filter1);
//        }


//        if (filter2 != null) {
//            // 주문번호 필터 적용
//            Long orderCode = Long.parseLong(filter2);
//            List<OrderDetailEntity> filteredByOrderCode = orderDetailRepository.findByOrderId_Ordercode(orderCode);
//            filterOrderlist.removeIf(order -> !order.getOrdercode().equals(orderCode));
//        }

        public List<OrderlistEntity> selOrder(Long orderCode){
            return orderlistRepository.findOrderById(orderCode);
        }
}

