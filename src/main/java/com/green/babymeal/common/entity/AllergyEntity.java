package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.annotation.Order;

@Entity
@Data
@Table(name = "allergy")
@Order(1)
public class AllergyEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성 방법중 하나 JPA 가 알아서 Autoincrement 해준다.
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED",length=20) //
    private Long allergyId;

    @Column(name = "allergyName",nullable = false,length=100)
    private String allergyName;


}
