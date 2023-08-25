package com.green.babymeal.common.entity;

import com.green.babymeal.common.config.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "t_user_pic")
@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserPicEntity extends BaseEntity  {
    @Id
    @ManyToOne
    @JoinColumn(name = "iuser")
    //@ToString.Exclude
    private UserEntity userEntity;

    @Id
    private String pic;
}
