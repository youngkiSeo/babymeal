package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "sale_volumn")
@Data
@NoArgsConstructor
@DynamicInsert
@SuperBuilder
@ToString(callSuper = true)
public class SaleVolumnEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_volumn_id" ,updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long saleVolumnId;

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductEntity productId;

    @Column(name = "count",length = 20)
    private int count;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDate createdAt;


}
