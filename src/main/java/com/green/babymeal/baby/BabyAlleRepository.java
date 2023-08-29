package com.green.babymeal.baby;

import com.green.babymeal.common.entity.UserBabyalleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BabyAlleRepository extends JpaRepository<UserBabyalleEntity, Long> {
}
