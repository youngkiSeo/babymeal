package com.green.babymeal.admin;

import com.green.babymeal.admin.model.*;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import com.green.babymeal.common.utils.MyFileUtils;

import org.springframework.data.domain.Pageable;
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
                                .productName(detail.getProductId().getPName())
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
                        .productName(orderDetails.get(1).getProductId().getPName())
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



    // 웹에디터

    public Long insPk(PkVo pkVo) {
        //웹에디터 시작 > 상품 pk번호 생성하여 반환 , ok
        adminMapper.insPk(pkVo);
        return pkVo.getProductId();
    }

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
        AdminProductImgDto dto=new AdminProductImgDto();
        dto.setProductId(productId);
        dto.setRandomName(randomName);
        adminMapper.insWebEditorImg(dto);
        ProductImgPkFull full=new ProductImgPkFull();
        full.setPImgId(dto.getPImgId());
        //String fullPath="http://192.168.0.144:5001/img/webeditor/"+productId+"/"+randomName;
        String fullPath="/webeditor/"+productId+"/"+randomName;
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
            AdminProductImgDto dto=new AdminProductImgDto();
            dto.setProductId(productId);
            dto.setRandomName(randomName);
            adminMapper.insWebEditorImgList(dto);
            ProductImgPkFull full=new ProductImgPkFull();
            full.setPImgId(dto.getPImgId());
            //String fullPath="http://192.168.0.144:5001/img/webeditor/"+productId+"/"+randomName;
            String fullPath="/webeditor/"+productId+"/"+randomName;
            full.setImg(fullPath);
            list.add(full);
        }
        return list;

    }


    public int updProduct(AdminProductUpdDto dto) {
        // 최종 상품 등록할때 사용되는 메소드
        if (dto.getCategory() > 4 && dto.getCategory() > 0){
            log.info("카테고리는 1-4까지 설정 가능, 확인 후 다시 입력하세요");
            return 0;
        }
        AdminProductCateRelationDto apcd=new AdminProductCateRelationDto();
        apcd.setProductId(dto.getProductId());
        apcd.setCateId(dto.getCategory());
        apcd.setCateDetailId(dto.getCateDetail());
        adminMapper.insProductCateRelation(apcd);
        return adminMapper.updAdminProduct(dto);
    }

    public int changeProduct(AdminProductUpdDto dto) {
        // 상품 정보 수정
        if (dto.getCategory() > 4 && dto.getCategory() > 0){
            log.info("카테고리는 1-4까지 설정 가능, 확인 후 다시 입력하세요");
            return 0;
        }
        return adminMapper.changeAdminProduct(dto);
    }

    public int delProductImg(Long productId) {
        String path = getAbsolutePath(fileDir) + "/webeditor/" + productId;
        MyFileUtils.delFolder(path);

        adminMapper.delImg(productId);
        return adminMapper.delProduct(productId);

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

    public int delWebEditorCancel(Long pImgId){
        ProductImgPk productImgPk = adminMapper.selProductImgPk(pImgId);
        System.out.println(productImgPk.getImg());
        String path = getAbsolutePath(fileDir) + "/webeditor/" + productImgPk.getProductId()+"/"+productImgPk.getImg();
        File file=new File(path);
        file.delete();
        return adminMapper.delWebEditorCancel(pImgId);
    }

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
            AdminProductImgDto dto=new AdminProductImgDto();
            dto.setProductId(productId);
            dto.setRandomName(randomName);
            adminMapper.insImgList(dto);
            ProductImgPkFull full=new ProductImgPkFull();
            full.setPImgId(dto.getPImgId());
            String fullPath="http://192.168.0.144:5001/img/product/"+productId+"/"+randomName;
            full.setImg(fullPath);
            list.add(full);
        }
        return list;
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
