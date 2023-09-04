package com.green.babymeal.common.repository;


import com.green.babymeal.common.entity.CateDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CateDetailRepository extends JpaRepository<CateDetailEntity,Long> {

}
