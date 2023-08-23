package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.annotation.Order;

@Entity
@Data
@Table(name = "email")
@Order(2)
public class EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false, columnDefinition = "BIGINT UNSIGNED") //
    private Long email_id;

    @OneToOne
    @JoinColumn(name = "iuser",nullable = false)
    private UserEntity iuser;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String ctnt;

}
