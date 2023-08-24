package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.annotation.Order;

@Entity
@Data
@Table(name = "cate_detail")
@Order(5)
public class CateDetailEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_detail_id",columnDefinition = "BIGINT UNSIGNED")
    private Long cateDetailId;

    @Column(name = "cate_name",nullable = false)
    private String cateDetailName;
}
