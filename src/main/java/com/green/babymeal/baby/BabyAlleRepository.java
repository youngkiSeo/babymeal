package com.green.babymeal.baby;

import com.green.babymeal.common.entity.BabyalleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BabyAlleRepository extends JpaRepository<BabyalleEntity, Long> {

    List<BabyalleEntity> findByUserBabyinfoEntity_BabyId(Long babyid);
}
