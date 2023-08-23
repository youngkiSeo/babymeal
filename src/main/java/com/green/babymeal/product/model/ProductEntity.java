package com.green.babymeal.product.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;

@Data
@Entity
@Order(1)
@Table(name = "product")
public class ProductEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long productId;

    @Column(name = "pTitle",nullable = false)
    private String pTitle;

    @Column(name = "pName",nullable = false)
    private String pName;

    @Column(name = "pPrice",nullable = false)
    private String pPrice;

    @ColumnDefault("0")
    @Column(name = "pQuantity")
    private String pQuantity;

    @Column(name = "description")
    private String description;

    @ColumnDefault("0")
    @Column(name = "isDelete")
    private String isDelete;

    @CreationTimestamp
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @ColumnDefault("0")
    @Column(name = "saleVoumn")
    private String saleVoumn;

    @ColumnDefault("0")
    @Column(name = "allergy")
    private String allergy;
}
