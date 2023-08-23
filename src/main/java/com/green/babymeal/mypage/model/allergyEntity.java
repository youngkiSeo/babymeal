package com.green.babymeal.mypage.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "allergy")
public class allergyEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED") //
    private Long allergyId;

    @Column(nullable = false)
    private String allergyname;


}
