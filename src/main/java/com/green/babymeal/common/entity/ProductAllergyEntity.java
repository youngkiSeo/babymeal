package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.annotation.Order;

@Data
@Entity
@Order(2)
@Table(name = "productAllergy")
public class ProductAllergyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED",length=20)
    private Long productAllergyId;

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductEntity productId;

    @ManyToOne
    @JoinColumn(name = "allergyId")
    private AllergyEntity allergyId;
}
