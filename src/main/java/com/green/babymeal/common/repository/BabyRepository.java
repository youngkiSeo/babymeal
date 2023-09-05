package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.UserBabyalleEntity;
import com.green.babymeal.common.entity.UserBabyinfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BabyRepository extends JpaRepository<UserBabyinfoEntity, Long> {

    List<UserBabyinfoEntity> findByUserEntity_Iuser(Long iuser);

}
