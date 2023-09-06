package com.green.babymeal.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "baby")
@Data
@NoArgsConstructor
@DynamicInsert
@ToString(callSuper = true)
@DynamicUpdate
public class UserBabyinfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long babyId;

    @Column(length = 20)
    private LocalDate childBirth;

    @Column(length = 100)
    private String prefer;

    @OneToMany(mappedBy = "userBabyinfoEntity", cascade = CascadeType.REMOVE) // mappedBy 컬럼이 안생기는 조인
    private List<UserBabyalleEntity> userBabyalleEntity;

    @ManyToOne
    @JoinColumn(name = "iuser")
    @ToString.Exclude
    private UserEntity userEntity;

}
