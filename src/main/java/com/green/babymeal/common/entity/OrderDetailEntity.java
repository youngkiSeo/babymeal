package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.core.annotation.Order;

@Data
@Entity
@Order(4)
@Table(name = "orderdetail")
@NoArgsConstructor
@SuperBuilder
public class OrderDetailEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED",length=20)
    private Long orderDetailId;

    @ManyToOne
    @JoinColumn(name = "orderId",columnDefinition = "BIGINT UNSIGNED")
    private OrderlistEntity orderId;

    @ManyToOne
    @JoinColumn(name = "productId",columnDefinition = "BIGINT UNSIGNED")
    private ProductEntity productId;

    @Column(name = "count",length=11)
    private int count;

    @Column(name = "totalPrice",nullable = false,length=11)
    private int totalPrice;

    @ColumnDefault("0")
    @Column(name = "delYn",length=4)
    private Byte delYn;
}
