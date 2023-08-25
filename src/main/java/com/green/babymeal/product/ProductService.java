package com.green.babymeal.product;

import com.green.babymeal.common.entity.ProductEntity;
import com.green.babymeal.common.entity.ReviewEntity;
import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.common.repository.ProductRepository;
import com.green.babymeal.product.model.ProductReviewDto;
import com.green.babymeal.product.model.ProductReviewVo;
import com.green.babymeal.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public int postReview(ProductReviewDto dto) {
        ProductEntity product = new ProductEntity();
        product.setProductId(dto.getProductId());
        UserEntity user = new UserEntity();
        user.setIuser(1L);

        // Review 생성
        ReviewEntity review = new ReviewEntity();
        review.setCtnt(dto.getCtnt());
        review.setProduct_id(product); // Product 객체 참조 설정
        review.setIuser(user); // User 객체 참조 설정
        reviewRepository.save(review);
        return 1;
    }
}
