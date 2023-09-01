package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "baby_table")
@Data
@NoArgsConstructor
@DynamicInsert
@ToString(callSuper = true)
public class BabyalleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "baby_allergy" ,updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long babyallergy;

    @ManyToOne
    @JoinColumn(name = "baby_id")
    @ToString.Exclude
    private BabyinfoEntity babyinfoEntity;


    @ManyToOne
    @JoinColumn(name = "allergyId")
    @ToString.Exclude
    private AllergyEntity allergyEntity;

}
