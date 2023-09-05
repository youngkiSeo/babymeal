package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.UserBabyalleEntity;
import com.green.babymeal.common.entity.UserBabyinfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BabyAlleRepository extends JpaRepository<UserBabyalleEntity, Long> {

    List<UserBabyalleEntity> findByUserBabyinfoEntity_BabyId(Long babyid);
    UserBabyalleEntity findByAllergyEntityAllergyIdAndUserBabyinfoEntity(Long allergyId, UserBabyinfoEntity entity);
}
