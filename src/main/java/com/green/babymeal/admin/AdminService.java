package com.green.babymeal.admin;

import com.green.babymeal.admin.model.*;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.*;

import com.green.babymeal.mypage.model.OrderlistDetailUserVo;
import com.green.babymeal.mypage.model.OrderlistDetailVo;
import com.green.babymeal.mypage.model.OrderlistUserVo;
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
import java.util.*;
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

    @Autowired
    private ProductThumbnailRepository productThumbnailRepository;


    public Page<OrderlistRes> allOrder(LocalDate startDate, LocalDate endDate,
                                       String filter1, String filter2, String filter3, String filter4,
                                       Pageable pageable) {
        Page<OrderlistEntity> outputOrderlist = orderlistRepository.findByCreatedAtBetween(startDate, endDate, pageable);
        List<OrderlistRes> resultList = new ArrayList<>();

        for (OrderlistEntity order : outputOrderlist.getContent()) {
            List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderId_OrderCode(order.getOrderCode());

            // 주문 관련 정보 추출
            String productName = "no data";
            if (!orderDetails.isEmpty()) {
                productName = orderDetails.get(0).getProductId().getPName();
            }

            UserVo userVoData = UserVo.builder()
                    .name((order.getIuser() != null) ? order.getIuser().getName() : "no data")
                    .iuser((order.getIuser() != null) ? order.getIuser().getIuser() : 0)
                    .build();

            List<OrderDetailVo> orderDetailVoList = orderDetails.stream()
                    .map(detail -> OrderDetailVo.builder()
                            .orderDetailId(detail.getOrderDetailId())
                            .productId((detail.getProductId() != null) ? detail.getProductId().getProductId() : 1)
                            .count(detail.getCount())
                            .totalPrice(detail.getTotalPrice())
                            .productName((detail.getProductId() != null) ? detail.getProductId().getPName() : "no data")
                            .build())
                    .collect(Collectors.toList());

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
                    .productName(productName)
                    .createdAt(order.getCreatedAt())
                    .iuser(order.getIuser().getIuser())
                    .userName(order.getIuser().getName())
                    .build();

            resultList.add(orderlistRes);
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
        if (filter4 != null) {
            resultList.removeIf(orderRes -> !orderRes.getShipment().equals(Long.parseLong(filter4)));
        }


        // size가 총 항목 수보다 큰 경우 size를 총 항목 수로 조정
        if (pageable.getPageSize() > outputOrderlist.getTotalElements()) {
            pageable = PageRequest.of(pageable.getPageNumber(), (int) outputOrderlist.getTotalElements());
        }

        return new PageImpl<>(resultList, pageable, outputOrderlist.getTotalElements());
    }


    public OrderlistDetailRes selOrder(Long orderCode) {
        OrderlistEntity byOrderCode = orderlistRepository.findByOrderCode(orderCode);

        List<OrderlistDetailVo> byOrderId = orderDetailRepository.findByOrderId(byOrderCode.getOrderId());

//        //주문정보 세팅
//        OrderlistUserVo vo = new OrderlistUserVo();
//        vo.setReciever(byOrderCode.getReciever());
//        vo.setAddress(byOrderCode.getAddress());
//        vo.setAddressDetail(byOrderCode.getAddressDetail());
//        vo.setPhoneNm(byOrderCode.getPhoneNm());
//        vo.setRequest(byOrderCode.getRequest());
//        vo.setUsepoint(byOrderCode.getUsepoint());

        //유저정보 세팅
        UserVo userVo = UserVo.builder()
                .iuser(byOrderCode.getIuser().getIuser())
                .name(byOrderCode.getIuser().getName())
                .build();


        OrderlistDetailRes data = OrderlistDetailRes.builder().orderDetailVo(byOrderId).userVo(userVo).build();
        data.setCount(byOrderId.get(0).getCount());

        // 대표상품, 전체가격
        data.setProductName(byOrderId.get(0).getPName());
        data.setTotalPrice(1000);


        return data;
    }



    // -------------------------------- 상품

    public Page<ProductAdminDto> allProduct(Pageable pageable) {
        Page<ProductEntity> productEntities = productRepository.findAll(pageable);
        List<ProductAdminDto> productAdminDtos = new ArrayList<>();
        List<Long> allergyIds = new ArrayList<>();

        // 데이터셋
        Map<Long, List<Long>> productDataMap = new HashMap<>();

        for (ProductEntity productEntity : productEntities) {
            // 알러지 정보 가져오기
            List<ProductAllergyEntity> productAllergies = productAllergyRepository.findByProductId_ProductId(productEntity.getProductId());
            allergyIds = productAllergies.stream()
                    .map(productAllergy -> productAllergy.getAllergyId().getAllergyId())
                    .collect(Collectors.toList());

            // 카테고리 정보 가져오기
            List<ProductCateRelationEntity> productCateRelationEntities = productCateRelationRepository.findByProductEntity_ProductId(productEntity.getProductId());
            List<Long> cateDetailIds = productCateRelationEntities.stream()
                    .map(relationEntity -> relationEntity.getCateDetailEntity().getCateDetailId())
                    .collect(Collectors.toList());

            productDataMap.put(productEntity.getProductId(), cateDetailIds);

        }

        // 다음 루프에서 중복을 방지하고 ProductAdminDto를 생성
        for (ProductEntity productEntity : productEntities) {

            List<ProductThumbnailEntity> thumbnailEntities = productThumbnailRepository.findByProductId(productEntity);

            // 썸네일 URL 목록을 생성
            List<String> thumbnailList = new ArrayList<>();
            for (ProductThumbnailEntity thumbnailEntity : thumbnailEntities) {
                thumbnailList.add(thumbnailEntity.getImg());
            }


            List<Long> cateDetailIds = productDataMap.get(productEntity.getProductId());
            Long cateId = null;
            if (cateDetailIds != null && !cateDetailIds.isEmpty()) {
                cateId = cateDetailIds.get(0);
            }

            ProductAdminDto productAdminDto = ProductAdminDto.builder()
                    .productId(productEntity.getProductId())
                    .name(productEntity.getPName())
                    .price(productEntity.getPPrice())
                    .cate(cateId) // 카테고리 1차
                    .cateDetail(cateDetailIds)
                    .allegyName(allergyIds)
                    .thumbnail(thumbnailList.isEmpty() ? "no data" : thumbnailList.get(0))
                    .build();
            productAdminDtos.add(productAdminDto);
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

            log.info("알러지 : {}", allergyIds);

            //카테고리 정보 가져오기

//            Long categoryId = null;
            List<Long> cateDetailIds = new ArrayList<>();

            List<ProductCateRelationEntity> productCateRelationEntities = productCateRelationRepository.findByProductEntity_ProductId(productEntity.getProductId());

            for (ProductCateRelationEntity relationEntity : productCateRelationEntities) {
                cateDetailIds.add(relationEntity.getCateDetailEntity().getCateDetailId());
            }

            List<ProductThumbnailEntity> thumbnailEntities = productThumbnailRepository.findByProductId(productEntity);

            // 썸네일 URL 목록을 생성
            List<String> thumbnailList = new ArrayList<>();
            for (ProductThumbnailEntity thumbnailEntity : thumbnailEntities) {
                thumbnailList.add(thumbnailEntity.getImg());
            }

            ProductAdminSelDto productAdminSelDto = ProductAdminSelDto.builder()
                    .productId(productEntity.getProductId())
                    .name(productEntity.getPName())
                    .price(productEntity.getPPrice())
                    .cate((productCateRelationEntities != null && !productCateRelationEntities.isEmpty())
                            ? productCateRelationEntities.get(0).getProductCateId()
                            : 0) // 카테고리-1차
                    .cateDetail(cateDetailIds) // 카테고리-2차
                    .allergyId(allergyIds)
                    .thumbnail(thumbnailList)
                    .build();
            return productAdminSelDto;

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
        entity.setIsDelete((byte)0);
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
            //String fullPath="http://192.168.0.144:5001/img/webeditor/"+productId+"/"+randomName; 전체경로
            String fullPath = "/img/webeditor/" + productId + "/" + randomName;
            full.setImg(fullPath);
            list.add(full);

        }
        return list;

    }


    // 최종 상품 등록할때 사용되는 메소드
    public int updProduct(AdminProductUpdDto dto) {


        AdminProductCateRelationDto apcd = new AdminProductCateRelationDto();
        apcd.setProductId(dto.getProductId());
        apcd.setCateId(dto.getCategory());
        apcd.setCateDetailId(dto.getCateDetail());
        adminMapper.insProductCateRelation(apcd);
        adminMapper.insertAllergyId(dto.getAllergyId(), dto.getProductId());
        return adminMapper.updAdminProduct(dto);
    }

    // 웹에디터 - 상품수정
    public int changeProduct(AdminProductUpdDto dto) {
        AdminProductCateRelationDto apcd = new AdminProductCateRelationDto();
        apcd.setProductId(dto.getProductId());
        apcd.setCateId(dto.getCategory());
        apcd.setCateDetailId(dto.getCateDetail());
        adminMapper.delCate(dto.getProductId()); // 상품의 카테고리정보 모두 삭제
        adminMapper.insProductCateRelation(apcd); // 상품의 카테고리정보 입력
        adminMapper.deleteAllergies(dto.getProductId()); // 상품의 알러지정보 모두 삭제
        adminMapper.updateAllergyId(dto.getAllergyId(), dto.getProductId()); // 상품의 알러지정보 입력
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


    //최종 상품 등록전에 이미지 삭제를 할 때
    @Transactional
    public int delWebEditorCancel(Long pImgId) {

        Optional<ProductImageEntity> byId = productImageRepository.findById(pImgId);
        if(byId.isEmpty()){
            productImageRepository.deleteById(pImgId);
            return 1;
        }
        ProductImageEntity productImageEntity = byId.get();
        String path = getAbsolutePath(fileDir) + "/webeditor/" + productImageEntity.getProductId().getProductId() + "/" + productImageEntity.getImg();
        File file = new File(path);
        file.delete();
        productImageRepository.deleteById(pImgId);
        return 1;
    }


    //썸네일 리스트
    @Transactional
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
            String fullPath = "/img/product/" + productId + "/" + randomName;
            full.setImg(fullPath);
            list.add(full);
        }
        return list;
    }

    // 썸네일 삭제
    public int deleteThumbnail(AdminThumbnailDelDto dto) {
        Long productId = dto.getProductId();
        String thumbnailFullName = dto.getThumbnailFullName();

        // 데이터베이스에서 해당 productId와 thumbnailFullName을 가진 썸네일을 찾아서 삭제
        ProductThumbnailEntity thumbnailToDelete = productThumbnailRepository.findByProductId_ProductIdAndImg(productId, thumbnailFullName);

        if (thumbnailToDelete != null) {
            String path = getAbsolutePath(fileDir) + "/product/" + productId + "/" + thumbnailToDelete.getImg();
            File file = new File(path);

            if (file.exists()) {
                if (file.delete()) {
                    // 파일 삭제 성공 시에만 데이터베이스 레코드 삭제
                    productThumbnailRepository.delete(thumbnailToDelete);
                    return 1;
                } else {
                    // 파일 삭제 실패
                    return -1;
                }
            } else {
                // 파일이 이미 존재하지 않음
                // 데이터베이스 레코드 삭제
                productThumbnailRepository.delete(thumbnailToDelete);
                return -2;
            }
        }
        return 0;
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
