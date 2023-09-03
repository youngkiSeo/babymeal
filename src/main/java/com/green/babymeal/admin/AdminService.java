package com.green.babymeal.admin;

import com.green.babymeal.admin.model.*;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AdminService {

    @Autowired
    private OrderlistRepository orderlistRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAllergyRepository productAllergyRepository;


    @Autowired
    private ProductCategoryRelationRepository productCateRelationRepository;



    public Page<OrderlistRes> allOrder(LocalDate startDate, LocalDate endDate,
                                       String filter1, String filter2, String filter3, String filter4,
                                       Pageable pageable) {
        // "필터1 : 검색어  필터2 : 주문번호  필터3 : 상품번호   필터4 : 주문상태"
        // 기간 내 주문리스트를 가져온다
        Page<OrderlistEntity> outputOrderlist = orderlistRepository.findByCreatedAtBetween(startDate, endDate, pageable);
        // 주문정보 담을 객체 생성
        List<OrderlistRes> resultList = new ArrayList<>();

        for (OrderlistEntity order : outputOrderlist.getContent()) {
            List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderId_OrderCode(order.getOrderCode());

            UserVo userVoData = UserVo.builder()
                    .name(order.getIuser().getName())
                    .iuser(order.getIuser().getIuser())
                    .build();

            List<OrderDetailVo> orderDetailVoList = new ArrayList<>();
            if (!orderDetails.isEmpty()) {
                orderDetailVoList = orderDetails.stream()
                        .map(detail -> OrderDetailVo.builder()
                                .orderDetailId(detail.getOrderDetailId())
                                .productId(detail.getProductId().getProductId())
                                .count(detail.getCount())
                                .totalPrice(detail.getTotalPrice())
                                .build())
                        .collect(Collectors.toList());
            }

            if (!orderDetails.isEmpty()) {
                OrderlistRes orderlistRes = OrderlistRes.builder()
                        .orderId(order.getOrderId())
                        .ordercode(order.getOrderCode())
                        .userVo(userVoData)
                        .payment(order.getPayment())
                        .shipment(order.getShipment())
                        .cancel(order.getCancel())
                        .phoneNm(order.getPhoneNm())
                        .request(order.getRequest())
                        .reciever(order.getReciever())
                        .address(order.getAddress())
                        .addressDetail(order.getAddressDetail())
                        .delYn(order.getDelYn())
                        .usepoint(order.getUsepoint())
                        .orderDetailVo(orderDetailVoList)
                        .build();
                resultList.add(orderlistRes); // 기본 데이터 조회 완료
            }

            if (filter2 != null) {
                resultList.removeIf(orderlistRes -> !orderlistRes.getOrdercode().equals(Long.parseLong(filter2)));
            }
        }
        return new PageImpl<>(resultList, outputOrderlist.getPageable(), outputOrderlist.getTotalElements());
    }


//        // 필터2 : 주문번호 기준 필터링
//        if (filter2 != null) {
//            resultList.removeIf(orderRes -> !orderRes.getOrdercode().equals(Long.parseLong(filter2)));
//        }
//
//        if (filter3 != null) {
//            resultList.removeIf(orderRes -> {
//                for (OrderDetailEntity orderDetail : orderRes.getOrderDetails()) {
//                    if (!orderDetail.getProductId().getProductId().equals(Long.parseLong(filter3))) {
//                        return true; // 상품번호가 일치하지 않으면 해당 주문 정보를 리스트에서 제거
//                    }
//                }
//                return false;
//            });
//        }
//
//        if (filter4 != null) {
//            resultList.removeIf(orderRes -> !orderRes.getPayment().equals(Long.parseLong(filter4)));
//        }

//        // Page 객체로 변환
//        Page<OrderlistRes> resultPage = new PageImpl<>(resultList, pageable, resultList.size());
//
//        return null;
//    }


//        if (filter1 != null) {
//            //검색어
//            filterOrderlist = applyFilter1(filterOrderlist, filter1);
//        }


    //        if (filter2 != null) {
//            // 주문번호 필터 적용
//            Long orderCode = Long.parseLong(filter2);
//            List<OrderDetailEntity> filteredByOrderCode = orderDetailRepository.findByOrderId_OrderCode(orderCode);
//            filterOrderlist.removeIf(order -> !order.getOrdercode().equals(orderCode));
//        }
//
    public List<OrderlistEntity> selOrder(Long orderCode) {
        return orderlistRepository.findOrderById(orderCode);
    }



    // -------------------------------- 상품

    public Page<ProductAdminDto> allProduct(Pageable pageable) {
        Page<ProductEntity> productEntities = productRepository.findAll(pageable);
        List<ProductAdminDto> productAdminDtos = new ArrayList<>();

        for (ProductEntity productEntity : productEntities) {
            // 알러지 정보 가져오기
            List<ProductAllergyEntity> productAllergies = productAllergyRepository.findByProductId_ProductId(productEntity.getProductId());
            List<Long> allergyIds = productAllergies.stream()
                    .map(productAllergy -> productAllergy.getAllergyId().getAllergyId())
                    .collect(Collectors.toList());

            //카테고리 정보 가져오기
            List<ProductCateRelationEntity> productCateRelationEntityList = productCateRelationRepository.findAll(); // 예시로 findAll() 메서드를 사용한 것으로 가정

            for (ProductCateRelationEntity relationEntity : productCateRelationEntityList) {
                ProductAdminDto productAdminDto2 = ProductAdminDto.builder()
                        .productId(relationEntity.getProductEntity().getProductId())
                        .name(productEntity.getPName())
                        .price(productEntity.getPPrice())
                        .cate(relationEntity.getCategoryEntity().getCateId()) // categoryEntity의 ID 값 설정
                        .cateDetail(relationEntity.getCateDetailEntity().getCateDetailId()) // cateDetailEntity의 ID 값 설정
                        .allegyName(allergyIds)
                        .thumbnail(Collections.singletonList(productEntity.getProductThumbnailEntityList().getImg()))
                        .build();

                productAdminDtos.add(productAdminDto2);
            }
        }
        return new PageImpl<>(productAdminDtos, pageable, productEntities.getTotalElements());
    }

    public ProductAdminSelDto selProduct(Long productId){
        ProductEntity productEntity = productRepository.findById(productId).orElse(null);
        if (productEntity != null) {
            // 알러지 정보 가져오기
            List<ProductAllergyEntity> productAllergies = productAllergyRepository.findByProductId_ProductId(productEntity.getProductId());
            List<Long> allergyIds = productAllergies.stream()
                    .map(productAllergy -> productAllergy.getAllergyId().getAllergyId())
                    .collect(Collectors.toList());

            //카테고리 정보 가져오기
            List<ProductCateRelationEntity> productCateRelationEntityList = productCateRelationRepository.findAll();

            Long categoryId = null;
            List<Long> cateDetailIds = new ArrayList<>();

            for (ProductCateRelationEntity relationEntity : productCateRelationEntityList) {
                categoryId = relationEntity.getCategoryEntity().getCateId();
                cateDetailIds.add(relationEntity.getCateDetailEntity().getCateDetailId());
            }

             ProductAdminSelDto dto = ProductAdminSelDto.builder()
                    .productId(productEntity.getProductId())
                    .name(productEntity.getPName())
                    .price(productEntity.getPPrice())
                    .cate(categoryId)
                    .cateDetail(cateDetailIds)
                    .allegyId(allergyIds)
                    .thumbnail(Collections.singletonList(productEntity.getProductThumbnailEntityList().getImg()))
                    .build();

            return dto;
        } else {
            return null;
        }
    }



    public Long webEditor(){
        ProductEntity entity=new ProductEntity();
        entity.setPName("");
        entity.setPPrice(0);
        ProductEntity save = productRepository.save(entity);
        if(save!=null){
            return entity.getProductId();
        }
        return 0L;
    }


}

//    public Page<ProductAdminDto> allProduct(Pageable pageable) {
//        return productRepository.findAll(pageable).map(productEntity -> {
//            List<String> allergyName = getAllergyNamesByProductId(productEntity.getProductId());
//
//            //카테고리 정보 불러오기
//            CategoryEntity category = productEntity.getProductCateRelationEntity().getCategoryEntity();
//            CateDetailEntity cateDetail = productEntity.getProductCateRelationEntity().getCateDetailEntity();
//
//            return ProductAdminDto.builder()
//                    .productId(productEntity.getProductId())
//                    .name(productEntity.getPName())
//                    .price(productEntity.getPPrice())
//                    //.description(productEntity.getDescription())
//                    //.quantity(productEntity.getPQuantity())
//                    .cate()
//                    .cateDetail()
//                    .allegyName(allergyName) //알러지 정보 추가
//                    .thumbnail(Collections.singletonList(productEntity.getProductThumbnailEntityList().getImg())) // 썸네일 이미지 추가
//                    .build();
//        });
//    }

//    public List<String> getAllergyNamesByProductId(Long productId) { // 알러지 이름으로 파싱
//        List<ProductAllergyEntity> productAllergyEntities = productAllergyRepository.findByProductId_ProductId(productId);
//
//        return productAllergyEntities.stream()
//                .map(productAllergyEntity -> productAllergyEntity.getAllergyId().getAllergyName())
//                .collect(Collectors.toList());
//    }
