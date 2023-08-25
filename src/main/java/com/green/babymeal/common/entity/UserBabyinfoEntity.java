package com.green.babymeal.common.entity;

import com.green.babymeal.common.config.security.model.GenderType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "baby")
@Data
@NoArgsConstructor
@DynamicInsert
@ToString(callSuper = true)
public class UserBabyinfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long baby_id;

    @Column(length = 20)
    private String birthday;

    @Column(name = "gender", length = 2)
    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @ManyToOne
    @JoinColumn(name = "iuser")
    @ToString.Exclude
    private UserEntity userEntity;

}
