package com.green.babymeal.product;

import com.green.babymeal.common.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {



}