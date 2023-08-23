package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cate_view")
public class CateViewEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_view_id")
    private Long cateViewId;

    @ManyToOne
    @JoinColumn(name = "cate_id")
    private CategoryEntity cateId;

    @ManyToOne
    @JoinColumn(name = "cate_detail_id")
    private CateDetailEntity cateDetailId;
}
