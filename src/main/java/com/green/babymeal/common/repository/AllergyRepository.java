package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.AllergyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyRepository extends JpaRepository<AllergyEntity, Long> {

}
