package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;


@Entity
@Data
@Table(name = "order_basket")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@SuperBuilder
public class OrderBasketEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long cartId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @ManyToOne
    @JoinColumn(name = "iuser")
    private UserEntity userEntity;

    private int count;

    @CreatedDate
    @Column(name = "created_at",nullable = false)
    private LocalDate createAt;

}
