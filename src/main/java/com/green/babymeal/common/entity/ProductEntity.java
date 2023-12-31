package com.green.babymeal.common.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Order(2)
@Table(name = "product")
@NoArgsConstructor
@SuperBuilder
public class ProductEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED",length=20)
    private Long productId;

    @Column(name = "pName",nullable = false,length=50)
    private String pName;

    @Column(name = "pPrice",nullable = false,length=11)
    private int pPrice;

    @ColumnDefault("0")
    @Column(name = "pQuantity",length=11)
    private int pQuantity;

    @Column(name = "description",length=1000)
    private String description;

    @ColumnDefault("0")
    @Column(name = "isDelete",length=4)
    private Byte isDelete;

    @CreationTimestamp
    @Column(name = "createdAt")
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private LocalDate updatedAt;

    @ColumnDefault("0")
    @Column(name = "saleVolume",length=11)
    private int saleVolume;

//    @ColumnDefault("0")
//    @Column(name = "allergy",length=4)
//    private Byte allergy;
    // 사용하지 않는 테이블임 !!

    @Column(name = "point_rate")
    private float pointRate;


    @OneToMany(mappedBy = "productId")
    @JsonManagedReference
    private List<ProductThumbnailEntity> productThumbnailEntityList=new ArrayList<>();
}
