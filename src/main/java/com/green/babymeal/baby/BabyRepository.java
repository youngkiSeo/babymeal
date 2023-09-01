package com.green.babymeal.baby;

import com.green.babymeal.common.entity.BabyinfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BabyRepository extends JpaRepository<BabyinfoEntity, Long> {

    List<BabyinfoEntity> findByUserEntity_Iuser(Long iuser);
}
