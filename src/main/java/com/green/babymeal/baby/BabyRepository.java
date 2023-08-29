package com.green.babymeal.baby;

import com.green.babymeal.common.entity.UserBabyinfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BabyRepository extends JpaRepository<UserBabyinfoEntity, Long> {
}
