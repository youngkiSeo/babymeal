package com.green.babymeal.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@Table(name = "product_thumbnail")
public class ProductThumbnailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_thumbnail_id", updatable = false,nullable = false, columnDefinition = "BIGINT UNSIGNED") //
    private Long thumbnailId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private ProductEntity productId;

    @Column
    private String img;
}
