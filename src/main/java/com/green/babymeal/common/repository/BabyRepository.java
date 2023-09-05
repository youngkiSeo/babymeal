package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.UserBabyinfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BabyRepository extends JpaRepository<UserBabyinfoEntity, Long> {

    List<UserBabyinfoEntity> findByUserEntity_Iuser(Long iuser);
    Optional<UserBabyinfoEntity> findById(Long babyId);
    // babyId로 조회하기 위한 메소드 선언
    UserBabyinfoEntity findByBabyId(Long babyId);

}
