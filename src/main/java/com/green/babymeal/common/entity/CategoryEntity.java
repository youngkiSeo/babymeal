package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "category")
public class CategoryEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id")
    private Long cateId;

    @Column(name = "cate_name",nullable = false)
    private String cateName;




}
