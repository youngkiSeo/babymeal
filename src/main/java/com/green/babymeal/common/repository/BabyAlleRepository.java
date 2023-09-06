package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.UserBabyalleEntity;
import com.green.babymeal.common.entity.UserBabyinfoEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BabyAlleRepository extends JpaRepository<UserBabyalleEntity, Long> {

    List<UserBabyalleEntity> findByUserBabyinfoEntity_BabyId(Long babyid);
    // 중복확인을 위한 메소드 선언
    @Query("SELECT ua FROM UserBabyalleEntity ua JOIN ua.userBabyinfoEntity bi WHERE bi.babyId = :babyId AND ua.allergyEntity.allergyId = :allergyId")
    UserBabyalleEntity findByBabyIdAndAllergyId(@Param("babyId") Long babyId, @Param("allergyId") Long allergyId);
    //UserBabyalleEntity findByAllergyEntityAllergyIdAndUserBabyinfoEntity(Long allergyId, UserBabyinfoEntity entity);

    void deleteByUserBabyinfoEntity_BabyId(Long babyId);


}
