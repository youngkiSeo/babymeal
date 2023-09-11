package com.green.babymeal.admin;

import com.green.babymeal.admin.model.*;
import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.*;

import com.green.babymeal.mypage.model.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twitter.penguin.korean.TwitterKoreanProcessorJava;
import com.twitter.penguin.korean.phrase_extractor.KoreanPhraseExtractor;
import com.twitter.penguin.korean.tokenizer.KoreanTokenizer;
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
import scala.collection.Seq;


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

    @Autowired
    private ProductCategoryRelationRepository productcaterelationRep;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    QSaleVolumnEntity saleVolumn = QSaleVolumnEntity.saleVolumnEntity;
    QProductThumbnailEntity thumbnail = QProductThumbnailEntity.productThumbnailEntity;


    public Page<OrderlistRes> allOrder(LocalDate startDate, LocalDate endDate,
                                       String filter1, String filter2, String filter3, String filter4, String filter5,
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
                    .delYn(order.getDelYn() != null ? 0 : order.getDelYn())
                    .usepoint(order.getUsepoint())
                    .orderDetailVo(orderDetailVoList)
                    .productName(productName)
                    .createdAt(order.getCreatedAt())
                    .iuser(order.getIuser().getIuser())
                    .userName(order.getIuser().getName())
                    .build();
            log.info("배송상태 {}", order.getShipment());

            resultList.add(orderlistRes);
        }



            //resultList.get(1).getOrderDetailVo().get(1).getProductName(); 의 index를 순회해야 함
        if (filter1 != null) { // 상품명 검색
            List<String> filter1Tokens = getTwiiterApiWord(filter1);
            log.info("리스트 : {}", filter1Tokens);

            // 주문 목록을 순회하면서 필터를 적용하고, 필터를 통과하지 못한 주문을 임시 목록에 저장
            List<OrderlistRes> filteredOrders = new ArrayList<>();

            for (OrderlistRes orderlistRes : resultList) {
                boolean keepOrder = false; // 주문을 유지할지 여부를 나타내는 플래그

                for (OrderDetailVo orderDetail : orderlistRes.getOrderDetailVo()) {
                    boolean containsToken = false; // 형태소를 포함하는지 여부를 나타내는 플래그
                    for (String token : filter1Tokens) {
                        if (orderDetail.getProductName().contains(token)) {
                            containsToken = true; // 주문이 형태소를 포함하면 플래그를 설정
                            break;
                        }
                    }

                    if (containsToken) {
                        keepOrder = true; // 주문이 형태소를 포함하면 유지
                        break;
                    }
                }

                if (keepOrder) {
                    filteredOrders.add(orderlistRes); // 주문을 유지해야하는 경우 임시 목록에 추가
                }
            }

            // 필터링된 주문 목록으로 결과를 업데이트
            // 바로 업데이트했더니 인덱스 오류나서 부득이 임시목록에서 값 옮겨담는 것으로 처리
            resultList = filteredOrders;
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
            log.info("필터 : {} , {} ", filter4, Long.parseLong(filter4));
            log.info("??? : {}", resultList.get(0).getShipment());

            resultList.removeIf(orderlistRes -> orderlistRes.getShipment() == null || orderlistRes.getShipment().longValue() != Long.parseLong(filter4));

            //resultList.removeIf(orderlistRes -> orderlistRes.getShipment() == null || !orderlistRes.getShipment().equals(filterValue));
        }

        //필터5 : 구매자이름
        if (filter5 != null) {
            resultList.removeIf(orderlistRes -> !orderlistRes.getUserVo().getName().equals(filter5));
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

        int totalCountPrice = 0; // 전체 가격 초기화
        for (OrderlistDetailVo orderDetail : byOrderId) {
            int productPrice = orderDetail.getPrice(); // 상품 가격
            int productCount = orderDetail.getCount();   // 상품 수량
            int productTotalPrice = productPrice * productCount; // 상품 총 가격

            totalCountPrice += productTotalPrice; // 전체 가격에 상품 총 가격 더하기
        }

        // 전체 혜택(적립금지급)
        int totalGivePoint = 0;

        for (OrderlistDetailVo orderDetail : byOrderId) {
            int productPrice = orderDetail.getPrice(); // 상품 가격
            int productCount = orderDetail.getCount(); // 상품 수량
            float pointRate = 0.005f; // 상품 적립율

            int productTotalPrice = productPrice * productCount; // 상품 총 가격
            int productGivePoint = (int) (productTotalPrice * pointRate); // 상품 적립금

            totalGivePoint += productGivePoint; // 전체 적립금에 상품 적립금 더하기
        }

        OrderlistDetailRes data = OrderlistDetailRes.builder()
                .productId(byOrderId.get(0).getProductId()) // 대표상품 productId
                .orderDetailId(byOrderCode.getOrderId())
                .usePoint(byOrderCode.getUsepoint())
                .shipment(byOrderCode.getShipment())
                .orderDetailVo(byOrderId)
                .count(byOrderId.get(0).getCount())
                .productName(byOrderId.get(0).getPName())
                .totalPrice(totalCountPrice) // 전체 가격 설정
                .givePoint(totalGivePoint)
                .build();

        return data;
    }

    // 배송상태변경
    @Transactional
    public int updateShipmentStatus(AdminShipmentDto shipmentDto) {
        List<Long> orderCodeList = shipmentDto.getOrderCode();
        byte newShipmentStatus = shipmentDto.getShipment();

        // 주문번호 목록으로 조회하여 데이터 가져옴
        List<OrderlistEntity> orderEntities = orderlistRepository.findByOrderCodeIn(orderCodeList);

        int updatedCount = 0; // 변경된 갯수 확인용

        // 배송 상태를 변경
        for (OrderlistEntity orderEntity : orderEntities) {
            orderEntity.setShipment(newShipmentStatus);
            updatedCount++;
        }

        return updatedCount; // 업데이트된 주문 수를 반환
    }

    // -------------------------------- 상품

    public Page<ProductAdminDto> allProduct(Pageable pageable) {
        Page<ProductEntity> productEntities = productRepository.findAll(pageable);
        List<ProductAdminDto> productAdminDtos = new ArrayList<>();

        // 데이터셋
        Map<Long, List<Long>> productDataMap = new HashMap<>();

        for (ProductEntity productEntity : productEntities) {
            // 각 제품에 대한 알러지 정보 가져오기
            List<Long> allergyIds = getAllergyIdsByProductId(productEntity.getProductId());

            // 카테고리 정보 가져오기
            List<ProductCateRelationEntity> productCateRelationEntities = productCateRelationRepository.findByProductEntity_ProductId(productEntity.getProductId());
            List<Long> cateDetailIds = productCateRelationEntities.stream()
                    .map(relationEntity -> relationEntity.getCateDetailEntity().getCateDetailId())
                    .collect(Collectors.toList());

            productDataMap.put(productEntity.getProductId(), allergyIds);

            // 다음 루프에서 중복을 방지하고 ProductAdminDto를 생성
            List<ProductThumbnailEntity> thumbnailEntities = productThumbnailRepository.findByProductId(productEntity);

            // 썸네일 URL 목록을 생성
            List<String> thumbnailList = new ArrayList<>();
            for (ProductThumbnailEntity thumbnailEntity : thumbnailEntities) {
                thumbnailList.add(thumbnailEntity.getImg());
            }

            //List<Long> cateDetailIdsForProduct = productDataMap.get(productEntity.getProductId());

            ProductAdminDto productAdminDto = ProductAdminDto.builder()
                    .productId(productEntity.getProductId())
                    .name(productEntity.getPName())
                    .price(productEntity.getPPrice())
                    .cate((productCateRelationEntities != null && !productCateRelationEntities.isEmpty())
                            ? productCateRelationEntities.get(0).getCategoryEntity().getCateId()
                            : 0) // 카테고리 1차
                    .cateDetail(cateDetailIds)
                    .allegyName(allergyIds)
                    .quantity(productEntity.getPQuantity())
                    .thumbnail(thumbnailList.isEmpty() ? "no data" : thumbnailList.get(0))
                    .delYn(productEntity.getIsDelete() == null ? 0 : productEntity.getIsDelete())
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
                    .delYn(productEntity.getIsDelete())
                    .quantity(productEntity.getPQuantity())
                    .cate((productCateRelationEntities != null && !productCateRelationEntities.isEmpty())
                            ? productCateRelationEntities.get(0).getCategoryEntity().getCateId()
                            : 0) // 카테고리-1차
                    .cateDetail(cateDetailIds) // 카테고리-2차
                    .description(productEntity.getDescription())
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
        entity.setIsDelete((byte) 0);
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
        String fullPath = /*"/img/webeditor/" + productId + "/" + */randomName;
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
            String fullPath = /*"/img/webeditor/" + productId + "/" + */randomName;
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

        List<Long> allergyIdList = dto.getAllergyId();
        if (allergyIdList != null && !allergyIdList.isEmpty()) {
            adminMapper.insertAllergyId(allergyIdList, dto.getProductId());
        }
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
        if (byId.isEmpty()) {
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
            String fullPath = /*"/img/product/" + productId + "/" + */randomName;
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

    //알러지 가져오는 메소드
    public List<Long> getAllergyIdsByProductId(Long productId) {
        return productAllergyRepository.findAllergyIdsByProductId(productId);
    }


    public SaleVolumnVoCount Selectsale(int page, int row, String year, String month) {
        LocalDate start = null;
        LocalDate end = null;
        if (month.equals("0")) {
            start = LocalDate.parse(year + "-01-01");
        } else {

        }
        start = LocalDate.parse(year + "-" + month + "-01");
        end = end(year, month);
        int offset = page * row;
        int totalprice = 0;


        List<SaleVolumnCount> saleVolumnCount = jpaQueryFactory.select(Projections.bean(
                        SaleVolumnCount.class, saleVolumn.productId.productId.countDistinct().as("productId")))
                .from(saleVolumn)
                .where(saleVolumn.createdAt.between(start, end)).fetch();

        //한달 총 매출 구하기
        List<SaleVolumnCount> salecount = jpaQueryFactory.select(Projections.bean(SaleVolumnCount.class, (saleVolumn.count.sum()).as("count"),
                        saleVolumn.productId.productId.as("productId")))
                .from(saleVolumn)
                .where(saleVolumn.createdAt.between(start, end))
                .groupBy(saleVolumn.productId.productId).fetch();


        for (int i = 0; i < salecount.size(); i++) {
            ProductEntity productEntity = productRepository.findById(salecount.get(i).getProductId()).get();
            int count = salecount.get(i).getCount();
            int result = count * productEntity.getPPrice();
            totalprice += result;

        }


        List<SaleVolumnVo> fetch = jpaQueryFactory.select(Projections.constructor(SaleVolumnVo.class,
                        saleVolumn.productId.productId,
                        saleVolumn.count.sum(),
                        saleVolumn.productId.pName,
                        saleVolumn.productId.pPrice,
                        thumbnail.img))
                .from(saleVolumn)
                .join(thumbnail)
                .on(saleVolumn.productId.productId.eq(thumbnail.productId.productId))
                .where(saleVolumn.createdAt.between(start, end))
                .groupBy(saleVolumn.productId.productId)
                .orderBy((saleVolumn.count.sum()).desc())
                .offset(offset)
                .limit(row)
                .fetch();

        for (int i = 0; i < fetch.size(); i++) {

            // 상품 가격 가져오기
            Long productId = fetch.get(i).getProductId();
            ProductEntity productEntity = productRepository.findById(productId).get();
            int pPrice = productEntity.getPPrice();
            int count = fetch.get(i).getCount();
            int productprice = count * pPrice;
            fetch.get(i).setPPrice(productprice);

            //카테고리 단계 붙이기
            List<ProductCateRelationEntity> byProductEntity = productcaterelationRep.findByProductEntity_ProductId(productEntity.getProductId());

            CategoryEntity categoryEntity = byProductEntity.get(0).getCategoryEntity();
            Long cateId = categoryEntity.getCateId();
            String name = "[" + cateId + "단계] " + productEntity.getPName();
            fetch.get(i).setPName(name);
        }

        SaleVolumnVoCount build = SaleVolumnVoCount.builder().vo(fetch).count(saleVolumnCount.get(0).getProductId()).totalprice(totalprice).build();
        return build;
    }

    public List<SaleVolumnColorVo> SelectsaleColor(String year, String month) {
        LocalDate start = LocalDate.parse(year + "-" + month + "-01");
        LocalDate end = end(year, month);

        List<SaleVolumnColorVo> fetch = jpaQueryFactory.select(Projections.bean(SaleVolumnColorVo.class,
                        saleVolumn.productId.productId.as("productId"),
                        saleVolumn.productId.pName.as("id"),
                        saleVolumn.productId.pName.as("label"),
                        (saleVolumn.count.sum()).as("value"),
                        saleVolumn.productId.pName.as("color")
                ))
                .from(saleVolumn)
                .where(saleVolumn.createdAt.between(start, end))
                .groupBy(saleVolumn.productId.productId)
                .orderBy((saleVolumn.count.sum()).desc())
                .limit(5)
                .fetch();

        for (int i = 0; i < fetch.size(); i++) {

            // 상품 가격 가져오기
            Long productId = fetch.get(i).getProductId();
            ProductEntity productEntity = productRepository.findById(productId).get();


            int pPrice = productEntity.getPPrice();
            int count = fetch.get(i).getValue();

            //카테고리 단계 붙이기
            List<ProductCateRelationEntity> byProductEntity = productcaterelationRep.findByProductEntity_ProductId(productEntity.getProductId());

            CategoryEntity categoryEntity = byProductEntity.get(0).getCategoryEntity();
            Long cateId = categoryEntity.getCateId();
            String name = "[" + cateId + "단계] " + productEntity.getPName();
            fetch.get(i).setId(name);
            fetch.get(i).setLabel(name);

            switch (i) {
                case 0:
                    fetch.get(i).setColor("hsl(340, 70%, 50%)");
                    break;
                case 1:
                    fetch.get(i).setColor("hsl(298, 70%, 50%)");
                    break;
                case 2:
                    fetch.get(i).setColor("hsl(280, 70%, 50%)");
                    break;
                case 3:
                    fetch.get(i).setColor("hsl(38, 70%, 50%)");
                    break;
                case 4:
                    fetch.get(i).setColor("hsl(321, 70%, 50%)");
                    break;
            }

        }
        return fetch;
    }

    public LocalDate end(String year, String month) {
        LocalDate end = null;

        if (month.equals("01") || month.equals("03") || month.equals("05") || month.equals("07") || month.equals("08") || month.equals("10") || month.equals("12")) {
            end = LocalDate.parse(year + "-" + month + "-31");
        } else if (month.equals("02")) {
            end = LocalDate.parse(year + "-" + month + "-28");
        } else
            end = LocalDate.parse(year + "-" + month + "-30");
        return end;
    }


    //트위터 형태소분석기
    public List<String> getTwiiterApiWord(String word) {
        CharSequence normalized = TwitterKoreanProcessorJava.normalize(word);
        Seq<KoreanTokenizer.KoreanToken> tokens = TwitterKoreanProcessorJava.tokenize(normalized);
        Seq<KoreanTokenizer.KoreanToken> stemmed = TwitterKoreanProcessorJava.stem(tokens);
        List<String> text = TwitterKoreanProcessorJava.tokensToJavaStringList(stemmed);
//        log.info("형태소 분석결과 :  {} " , word);
//        StringBuffer sb = new StringBuffer();
//        log.info( "  : : {}", text.size());
//        if (text.size() > 0){
//            for (int i = 0; i <text.size(); i++) {
//                sb.append(text.get(i)).append("|");
//            }
//        }
//        log.info("형태소 분석결과 :  {} " , sb.toString());
        return text;
    }
}

