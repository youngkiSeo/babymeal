package com.green.babymeal.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.babymeal.common.config.security.model.ProviderType;
import com.green.babymeal.common.config.security.model.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Data
@Table(name = "user",uniqueConstraints = {@UniqueConstraint(columnNames = {"uid","nick_nm"})})
@NoArgsConstructor
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long iuser;

    @Column(name = "password")
    private String password;

    @Column(name = "image")
    private String image;

    @Column(name = "name")
    private String name;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "mobile_nb")
    private String mobile_nb;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @Column(name="role_type", length=20)
    @Enumerated(EnumType.STRING) // Enum이란 final이랑 비슷하다. 특정한 값을 사용할때 사용되는 값 이외의 값이 안들어 가게 할수있다.
    @NotNull
    private RoleType roleType;

    @Column(nullable = false, length = 80)
    @Size(min = 3, max = 80)
    private String uid;

    @Column(name="provider_type", length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    @Size(min = 10 , max = 20) // java 단 에서의 사이즈이다.
    @ToString.Exclude
    private ProviderType providerType; // 자동으로 스네이크 기법으로 변경 해준다.

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "address")
    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "nick_nm")
    private String nickNm;

    @ColumnDefault("0")
    @Column(name = "point")
    private int point;

    @ColumnDefault("0")
    @Column(name = "del_yn")
    private Byte delYn;


   /*@OneToMany(mappedBy = "userEntity")
   @ToString.Exclude
   private List<OrderBasketEntity> orderBasketEntityList=new ArrayList<>();*/


}
