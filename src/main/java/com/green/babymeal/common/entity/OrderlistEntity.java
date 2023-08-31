package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;

@Entity
@Data
@Order(3)
@Table(name = "order_list")
public class OrderlistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED",length=100)
    private Long orderId;

    @Column(name = "order_code",unique = true,updatable = false,nullable = false)
    private Long orderCode;

    @ManyToOne
    @JoinColumn(name = "iuser",nullable = false)
    private UserEntity iuser;

    @ColumnDefault("1")
    @Column(name = "payment",nullable = false,length=3)
    private Byte payment;

    @ColumnDefault("1")
    @Column(name = "shipment",nullable = false,length=3)
    private Byte shipment;

    @ColumnDefault("0")
    @Column(name = "cancel",length=3)
    private Byte cancel;

    @Column(name = "createdAt",nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "phoneNm",nullable = false,length=50)
    private String phoneNm;

    @Column(name = "request",nullable = false,length=100)
    private String request;

    @Column(name = "reciever",nullable = false,length=20)
    private String reciever;

    @Column(name = "address",nullable = false,length=50)
    private String address;

    @Column(name = "addressDetail",nullable = false,length=50)
    private String addressDetail;

    @ColumnDefault("0")
    @Column(name = "delYn",length=4)
    private Byte delYn;

    @ColumnDefault("0")
    @Column(name = "usepoint",length=11)
    private int usepoint;

}
