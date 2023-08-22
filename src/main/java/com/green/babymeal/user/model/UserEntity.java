package com.green.babymeal.user.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user",uniqueConstraints = {@UniqueConstraint(columnNames = "email")})

public class UserEntity {

    @Id @GeneratedValue
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


}
