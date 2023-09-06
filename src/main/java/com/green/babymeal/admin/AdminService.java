package com.green.babymeal.admin;

import com.green.babymeal.admin.model.*;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.green.babymeal.common.utils.MyFileUtils;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


import static com.green.babymeal.common.utils.MyFileUtils.getAbsolutePath;


@Slf4j
@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Value("${file.dir}")
    private String fileDir;

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

    @Autowired
    private ProductImageRepository productImageRepository;


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
                    .name((order.getIuser() != null) ? order.getIuser().getName() : "no data")
                    .iuser((order.getIuser() != null) ? order.getIuser().getIuser() : 0)
                    .build();

            List<OrderDetailVo> orderDetailVoList = new ArrayList<>();
            if (!orderDetails.isEmpty()) {
                orderDetailVoList = orderDetails.stream()
                        .map(detail -> OrderDetailVo.builder()
                                .orderDetailId(detail.getOrderDetailId())
                                .productId((detail.getProductId() != null) ? detail.getProductId().getProductId() : 1)
                                .count(detail.getCount())
                                .totalPrice(detail.getTotalPrice())
                                .productName((detail.getProductId() != null) ? detail.getProductId().getPName() : "no data")
                                .build())
                        .collect(Collectors.toList());
            } else {
                // OrderDetail이 없는 경우 "no data"로 처리
                orderDetailVoList.add(OrderDetailVo.builder()
                        .orderDetailId(0L)
                        .productId(0L)
                        .count(0)
                        .totalPrice(0)
                        .productName("no data")
                        .build());
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
                        .productName((orderDetails.size() > 1) ? orderDetails.get(1).getProductId().getPName() : "no data")
                        .build();
                resultList.add(orderlistRes);
            } else {
                // OrderDetail이 없는 경우 "no data"로 처리
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
                        .productName("no data")
                        .build();
                resultList.add(orderlistRes);
            }
        }

        // 필터2 : 주문번호 기준 필터링
        if (filter2 != null) {
            resultList.removeIf(orderRes -> !orderRes.getOrdercode().equals(Long.parseLong(filter2)));
        }

        // 필터3 : 상품번호 , 주문 정보를 필터링할 때 filter3 값과 일치하는 상품 번호가 있는 경우 해당 주문 정보를 유지, 그렇지 않은 경우에만 제거
        if (filter3 != null) {
            resultList.removeIf(orderRes -> {
                boolean filterTemp = false;
                for (OrderDetailVo orderDetail : orderRes.getOrderDetailVo()) {
                    if (orderDetail.getProductId().equals(Long.parseLong(filter3))) {
                        filterTemp = true;
                        break;
                    }
                }
                return !filterTemp;
            });
        }

        //필터4 : 주문상태
        if (filter4 != null){
            resultList.removeIf(orderRes -> !orderRes.getShipment().equals(Long.parseLong(filter4)));
        }


        // size가 총 항목 수보다 큰 경우 size를 총 항목 수로 조정
        if (pageable.getPageSize() > outputOrderlist.getTotalElements()) {
            pageable = PageRequest.of(pageable.getPageNumber(), (int) outputOrderlist.getTotalElements());
        }

        return new PageImpl<>(resultList, pageable, outputOrderlist.getTotalElements());
    }






    public List<OrderlistDetailRes> selOrder(Long orderCode) {
        // 주문정보 담을 객체 생성
        List<OrderlistDetailRes> resultList = new ArrayList<>();

        // OrderlistEntity 객체를 조회하여 필요한 정보 얻기
        List<OrderlistEntity> orderlistEntities = orderlistRepository.findByOrderCode(orderCode);

        for (OrderlistEntity order : orderlistEntities) {
            List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderId_OrderCode(order.getOrderId());

            UserVo userVoData = UserVo.builder()
                    .name((order.getIuser() != null) ? order.getIuser().getName() : "no data")
                    .iuser((order.getIuser() != null) ? order.getIuser().getIuser() : 0)
                    .build();

            List<OrderDetailVo> orderDetailVoList = new ArrayList<>();
            if (!orderDetails.isEmpty()) {
                orderDetailVoList = orderDetails.stream()
                        .map(detail -> OrderDetailVo.builder()
                                .orderDetailId(detail.getOrderDetailId())
                                .productId((detail.getProductId() != null) ? detail.getProductId().getProductId() : 1)
                                .count(detail.getCount())
                                .totalPrice(detail.getTotalPrice())
                                .productName((detail.getProductId() != null) ? detail.getProductId().getPName() : "no data")
                                .build())
                        .collect(Collectors.toList());
            } else {
                // OrderDetail이 없는 경우 "no data"로 처리
                orderDetailVoList.add(OrderDetailVo.builder()
                        .orderDetailId(0L)
                        .productId(0L)
                        .count(0)
                        .totalPrice(0)
                        .productName("no data")
                        .build());
            }
////
////            // OrderlistDetailRes 객체 생성 및 결과 리스트에 추가
////            OrderlistDetailRes orderDetailRes = OrderlistDetailRes.builder()
////                    .orderDetailId(order.getOrderId())
////                    .ordercode(order.getOrderCode())
////                    .userVo(userVoData)
////                    .payment(order.getPayment())
////                    .shipment(order.getShipment())
////                    .cancel(order.getCancel())
////                    .phoneNm(order.getPhoneNm())
////                    .request(order.getRequest())
////                    .reciever(order.getReciever())
////                    .address(order.getAddress())
////                    .addressDetail(order.getAddressDetail())
////                    .delYn(order.getDelYn())
////                    .usepoint(order.getUsepoint())
////                    .orderDetailVo(orderDetailVoList)
////                    .productName((orderDetails.size() > 1) ? orderDetails.get(1).getProductId().getPName() : "no data")
////                    .build();
//
//            resultList.add(orderDetailRes);
        }

        return resultList;
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

            // 카테고리 정보 가져오기
            List<ProductCateRelationEntity> productCateRelationEntityList = productCateRelationRepository.findAll();
            List<Long> cateDetailIds = new ArrayList<>();

            List<ProductCateRelationEntity> productCateRelationEntities = productCateRelationRepository.findByProductEntity_ProductId(productEntity.getProductId());

            for (ProductCateRelationEntity relationEntity : productCateRelationEntities) {
                cateDetailIds.add(relationEntity.getCateDetailEntity().getCateDetailId());
            }


            for (ProductCateRelationEntity relationEntity : productCateRelationEntityList) {


                ProductAdminDto productAdminDto2 = ProductAdminDto.builder()
                        .productId(relationEntity.getProductEntity().getProductId())
                        .name(productEntity.getPName())
                        .price(productEntity.getPPrice())
                        .cate(relationEntity.getCategoryEntity().getCateId())
                        .cateDetail(cateDetailIds) // cateDetailIds 리스트를 cateDetail에 설정
                        .allegyName(allergyIds)
                        .thumbnail(Collections.singletonList(
                                (productEntity.getProductThumbnailEntityList() != null)
                                        ? productEntity.getProductThumbnailEntityList().getImg()
                                        : "no data"
                        ).toString())
                        .build();

                productAdminDtos.add(productAdminDto2);
            }
        }

        // size가 총 갯수보다 크면 max고정
        if (pageable.getPageSize() > productEntities.getTotalElements()) {
            pageable = PageRequest.of(pageable.getPageNumber(), (int) productEntities.getTotalElements());
        }
        return new PageImpl<>(productAdminDtos, pageable, productEntities.getTotalElements());
    }

    public ProductAdminSelDto selProduct(Long productId) {
        ProductEntity productEntity = productRepository.findById(productId).orElse(null);
        if (productEntity != null) {
            // 알러지 정보 가져오기
            List<ProductAllergyEntity> productAllergies = productAllergyRepository.findByProductId_ProductId(productEntity.getProductId());
            List<Long> allergyIds = productAllergies.stream()
                    .map(productAllergy -> productAllergy.getAllergyId().getAllergyId())
                    .collect(Collectors.toList());

            //카테고리 정보 가져오기

            Long categoryId = null;
            List<Long> cateDetailIds = new ArrayList<>();

            List<ProductCateRelationEntity> productCateRelationEntities = productCateRelationRepository.findByProductEntity_ProductId(productEntity.getProductId());

            for (ProductCateRelationEntity relationEntity : productCateRelationEntities) {
                cateDetailIds.add(relationEntity.getCateDetailEntity().getCateDetailId());
            }

            ProductAdminSelDto dto = ProductAdminSelDto.builder()
                    .productId(productEntity.getProductId())
                    .name(productEntity.getPName())
                    .price(productEntity.getPPrice())
                    .cate(categoryId)
                    .cateDetail(cateDetailIds)
                    .allegyId(allergyIds)
                    .thumbnail(productEntity.getProductThumbnailEntityList().getImg()) // 리스트
                    .build();

            return dto;
        } else {
            return null;
        }
    }


    // 웹에디터
    public Long webEditorPk() {
        //웹에디터 시작 > 상품 pk번호 생성하여 반환 , ok
        ProductEntity entity = new ProductEntity();
        entity.setPPrice(0);
        entity.setPName("");
        ProductEntity save = productRepository.save(entity);
        if (save != null) {
            return save.getProductId();
        }
        return 0L;
    }

    //이미지 1개만 넣기
    public ProductImgPkFull insWebEditorImg(MultipartFile img, Long productId) {

        String path = getAbsolutePath(fileDir) + "/webeditor/" + productId;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String randomName = MyFileUtils.getRandomFileNm(img.getOriginalFilename());
        String fileUpload = path + "/" + randomName;
        File file1 = new File(fileUpload);
        try {
            img.transferTo(file1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AdminProductImgDto dto = new AdminProductImgDto();
        dto.setProductId(productId);
        dto.setRandomName(randomName);

        ProductImageEntity productImageEntity = new ProductImageEntity();
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(productId);
        productImageEntity.setImg(randomName);
        productImageEntity.setProductId(productEntity);

        ProductImageEntity save = productImageRepository.save(productImageEntity);
        ProductImgPkFull full = new ProductImgPkFull();
        full.setPImgId(save.getP_img_id());
        //String fullPath="http://192.168.0.144:5001/img/webeditor/"+productId+"/"+randomName;
        String fullPath = "/img/webeditor/" + productId + "/" + randomName;
        full.setImg(fullPath);
        return full;
    }


    // 이미지 리스트로넣기
    // 현재상태 : 리스트로 여러개 들어가야하는데 1개씩 넣을때만 에러없이 정상동작됨
    // 여러개 넣으면 1개만 들어가고 에러발생. 수정중
    public List<ProductImgPkFull> insWebEditorImgList(List<MultipartFile> img, Long productId) {
        String path = getAbsolutePath(fileDir) + "/webeditor/" + productId;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        List<ProductImgPkFull> list = new ArrayList();
        for (MultipartFile imgfile : img) {
            String randomName = MyFileUtils.getRandomFileNm(imgfile.getOriginalFilename());
            String fileUpload = path + "/" + randomName;
            File file1 = new File(fileUpload);
            try {
                imgfile.transferTo(file1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ProductImageEntity productImageEntity = new ProductImageEntity();
            productImageEntity.setImg(randomName);
            ProductEntity productEntity = new ProductEntity();
            productEntity.setProductId(productId);
            productImageEntity.setProductId(productEntity);

            ProductImageEntity save = productImageRepository.save(productImageEntity);
            ProductImgPkFull full = new ProductImgPkFull();
            full.setPImgId(save.getP_img_id());
            //String fullPath="http://192.168.0.144:5001/img/webeditor/"+productId+"/"+randomName;
            String fullPath = "/img/webeditor/" + productId + "/" + randomName;
            full.setImg(fullPath);
            list.add(full);

        }
        return list;

    }


    // 최종 상품 등록할때 사용되는 메소드
    public int updProduct(AdminProductUpdDto dto) {

        if (dto.getCategory() > 4 && dto.getCategory() > 0) {
            log.info("카테고리는 1-4까지 설정 가능, 확인 후 다시 입력하세요");
            return 0;
        }
        AdminProductCateRelationDto apcd = new AdminProductCateRelationDto();
        apcd.setProductId(dto.getProductId());
        apcd.setCateId(dto.getCategory());
        apcd.setCateDetailId(dto.getCateDetail());
        adminMapper.insProductCateRelation(apcd);
        return adminMapper.updAdminProduct(dto);
    }

    public int changeProduct(AdminProductUpdDto dto) {
        // 상품 정보 수정
        if (dto.getCategory() > 4 && dto.getCategory() > 0) {
            log.info("카테고리는 1-4까지 설정 가능, 확인 후 다시 입력하세요");
            return 0;
        }
        return adminMapper.changeAdminProduct(dto);
    }


    //상품 등록을 취소할 때 상품 레코드와 사진 파일을 삭제한다
    @Transactional
    public int delProductImg(Long productId) {
        String path = getAbsolutePath(fileDir) + "/webeditor/" + productId;
        MyFileUtils.delFolder(path);

        productImageRepository.deleteByProductId_ProductId(productId);
        productRepository.deleteById(productId);
        return 1;
    }

    public List<AdminProductEntity> getProduct(int productId) {
        return adminMapper.getProduct(productId);
    }

    public int delAdminProduct(int productId) {
        return adminMapper.delAdminProduct(productId);
    }

    public AdminProductUpdDto updProductInfo(int productId) {
        List<Integer> cateDetailList = adminMapper.updProductInfoCate(productId); // 카테고리 정보 획득
        AdminProductUpdDto adminProductUpdDto = adminMapper.updProductInfo(productId); // 상품 정보 획득
        adminProductUpdDto.setCateDetail(cateDetailList); // 카테고리 정보를 AdminProductUpdDto에 설정
        return adminProductUpdDto;
    }


    //최종상품 등록전에 이미지 삭제를 할 때
    @Transactional
    public int delWebEditorCancel(Long pImgId) {
        ProductImageEntity productImageEntity = productImageRepository.findById(pImgId).get();
        String path = getAbsolutePath(fileDir) + "/webeditor/" + productImageEntity.getProductId() + "/" + productImageEntity.getImg();
        File file = new File(path);
        file.delete();
        productImageRepository.deleteById(pImgId);
        return 1;
    }

    //썸네일 리스트
    public List<ProductImgPkFull> insImgList(List<MultipartFile> img, Long productId) {
        String path = getAbsolutePath(fileDir) + "/product/" + productId;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        List<ProductImgPkFull> list = new ArrayList();
        for (MultipartFile imgfile : img) {
            String randomName = MyFileUtils.getRandomFileNm(imgfile.getOriginalFilename());
            String fileUpload = path + "/" + randomName;
            File file1 = new File(fileUpload);
            try {
                imgfile.transferTo(file1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            AdminProductImgDto dto = new AdminProductImgDto();
            dto.setProductId(productId);
            dto.setRandomName(randomName);
            adminMapper.insImgList(dto);
            ProductImgPkFull full = new ProductImgPkFull();
            full.setPImgId(dto.getPImgId());
            String fullPath = "http://192.168.0.144:5001/img/product/" + productId + "/" + randomName;
            full.setImg(fullPath);
            list.add(full);
        }
        return list;
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
//
//    public List<String> getAllergyNamesByProductId(Long productId) { // 알러지 이름으로 파싱
//        List<ProductAllergyEntity> productAllergyEntities = productAllergyRepository.findByProductId_ProductId(productId);
//
//        return productAllergyEntities.stream()
//                .map(productAllergyEntity -> productAllergyEntity.getAllergyId().getAllergyName())
//                .collect(Collectors.toList());
//    }
}
