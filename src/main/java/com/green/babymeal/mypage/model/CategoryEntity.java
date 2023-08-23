package com.green.babymeal.mypage.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.annotation.Order;

@Data
@Entity
@Order(1)
@Table(name = "category")
public class CategoryEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long cateId;

    @Column(name = "cateName",nullable = false)
    private String cateName;

}
