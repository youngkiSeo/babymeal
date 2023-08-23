package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cate_detail")
public class CateDetailEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_detail_id")
    private Long cateDetailId;

    @Column(name = "cate_name",nullable = false)
    private String cateDetailName;
}
