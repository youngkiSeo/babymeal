package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.core.annotation.Order;

@Entity
@Data
@Table(name = "category")
@Order(6)
@ToString
public class CategoryEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id",columnDefinition = "BIGINT UNSIGNED")
    private Long cateId;

    @Column(name = "cate_name",nullable = false)
    private String cateName;




}
