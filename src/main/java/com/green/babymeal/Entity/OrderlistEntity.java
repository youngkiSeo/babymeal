package com.green.babymeal.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;

@Entity
@Data
@Order(2)
@Table(name = "order_list")
public class OrderlistEntity {

    @Id
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long orderid;

    @ManyToOne
    @JoinColumn(name = "iuser",nullable = false)
    private UserEntity iuser;

    @Column(name = "payment",nullable = false)
    private int payment;

    @Column(name = "shipment",nullable = false)
    private int shipment;

    @ColumnDefault("0")
    @Column(name = "cancel")
    private int cancel;

    @Column(name = "createdAt",nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "phoneNm",nullable = false)
    private String phoneNm;

    @Column(name = "request",nullable = false)
    private String request;

    @Column(name = "reciever",nullable = false)
    private String reciever;

    @Column(name = "address",nullable = false)
    private String address;

    @Column(name = "addressDetail",nullable = false)
    private String addressDetail;

    @ColumnDefault("0")
    @Column(name = "delYn")
    private int delYn;

    @ColumnDefault("0")
    @Column(name = "userpoint")
    private int userpoint;

}
