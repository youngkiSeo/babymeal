package com.green.babymeal.product;

import com.green.babymeal.common.entity.*;
import com.green.babymeal.common.repository.ProductAllergyRepository;
import com.green.babymeal.common.repository.ProductRepository;
import com.green.babymeal.common.repository.ReviewRepository;
import com.green.babymeal.product.model.ProductReviewDto;
import com.green.babymeal.product.model.ProductSelDto;
import com.green.babymeal.product.model.ProductVolumeDto;
import com.green.babymeal.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {


    private final Long USERPk = 1L; // 테스트용 임시 유저 pk
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAllergyRepository ProductAllergyRepository;

    @Autowired
    private UserRepository userRepository;

    public int postReview(ProductReviewDto dto) {
        ProductEntity product = new ProductEntity();
        product.setProductId(dto.getProductId());
        UserEntity user = new UserEntity();
        user.setIuser(USERPk);

        // Review 생성
        ReviewEntity review = new ReviewEntity();
        review.setCtnt(dto.getCtnt());
        review.setProductId(product); // Product 객체 참조 설정
        review.setIuser(user); // User 객체 참조 설정
        reviewRepository.save(review);
        return 1;
    }

    public List<ReviewEntity> getReviewById(Long productId) {
        ProductEntity entity = new ProductEntity();
        entity.setProductId(productId);
        return reviewRepository.findAllByProductId(entity);
    }

    public List<ProductVolumeDto> selProductVolumeYearMonth(int year, int month) {
        return productRepository.findSaleVolume(year, month);
    }

    public ProductSelDto selProduct(Long productId) {
        // 상품 ID로부터 상품 정보를 조회
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지않는상품입니다 : " + productId)); // 예외처리
        // 상품과 관련된 알러지 정보 조회
        List<ProductAllergyEntity> productAllergies = ProductAllergyRepository.findByProductId_ProductId(productId);
        // 알러지 정보를 문자열 리스트로 변환
        List<String> allergyName = productAllergies.stream()
                .map(productAllergyEntity -> getAllergyName(productAllergyEntity.getAllergyId()))
                .collect(Collectors.toList());
        // 조회된 상품 정보와 알러지 정보를 매핑하여 ProductSelDto 객체 생성
        ProductSelDto productAllergyDto = new ProductSelDto();
        productAllergyDto.setPName(productEntity.getPName());
        productAllergyDto.setDescription(productEntity.getDescription());
        productAllergyDto.setPPrice(productEntity.getPPrice());
        productAllergyDto.setPQuantity(productEntity.getPQuantity());
        productAllergyDto.setSaleVoumn(productEntity.getSaleVolume());
        productAllergyDto.setAllergyNames(allergyName);
        return productAllergyDto;
    }

    private String getAllergyName(AllergyEntity allergyEntity) {
        // 알러지id로 알러지 종류(이름) 매칭
        if (allergyEntity == null) {
            return null;
        }
        return allergyEntity.getAllergyName();
    }
}
