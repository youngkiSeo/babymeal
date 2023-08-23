package com.green.babymeal.Entity;

import jakarta.persistence.*;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user",uniqueConstraints = {@UniqueConstraint(columnNames = {"email","nick_nm"})})
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long iuser;


    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "image")
    private String image;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "birthday",nullable = false)
    private LocalDateTime birthday;

    @Column(name = "mobile_nb",nullable = false)
    private String mobile_nb;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "role",nullable = false)
    private String role;


    @Column(name = "secret_key")
    private String secret_key;

    @Column(name = "zip_code",nullable = false)
    private String zipCode;

    @Column(name = "address")
    private String address;

    @Column(name = "address_detail",nullable = false)
    private String addressDetail;

    @Column(name = "nick_nm")
    private String nickNm;

    @ColumnDefault("0")
    @Column(name = "point")
    private int point;

    @ColumnDefault("0")
    @Column(name = "del_yn",nullable = false)
    private char delYn;




}
