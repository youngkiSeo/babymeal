package com.green.babymeal.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@Table(name = "order_basket")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@SuperBuilder
public class OrderBasketEntity  {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iuser")
    @JsonBackReference
    private UserEntity userEntity;

    private int count;

    @CreatedDate
    @Column(name = "created_at",nullable = false)
    private LocalDate createAt;

}
