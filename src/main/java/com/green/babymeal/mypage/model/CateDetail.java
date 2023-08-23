package com.green.babymeal.mypage.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cateDetail")
public class CateDetail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long cateDetailId;

    @Column(name = "cateName",nullable = false)
    private String cateName;
}
