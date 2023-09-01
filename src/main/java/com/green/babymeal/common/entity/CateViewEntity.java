package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.annotation.Order;

@Entity
@Data
@Table(name = "cate_view")
@Order(7)
public class CateViewEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_view_id",columnDefinition = "BIGINT UNSIGNED")
    private Long cateViewId;

    @ManyToOne
    @JoinColumn(name = "cate_id",columnDefinition = "BIGINT UNSIGNED")
    private CategoryEntity categoryEntity;


    @ManyToOne
    @JoinColumn(name = "cate_detail_id",columnDefinition = "BIGINT UNSIGNED")
    private CateDetailEntity cateDetailEntity;
}
