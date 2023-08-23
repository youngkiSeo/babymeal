package com.green.babymeal.mypage.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.annotation.Order;

@Data
@Entity
@Order(2)
@Table(name = "cateView")
public class CateViewEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long cateViewId;

    @ManyToOne
    @JoinColumn(name = "cateId",nullable = false)
    private CategoryEntity cateId;

    @ManyToOne
    @JoinColumn(name = "cateDetailId",nullable = false)
    private CateDetail cateDetailId;
}
