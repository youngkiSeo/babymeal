package com.green.babymeal.common.repository;

import com.green.babymeal.common.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CateRepository extends JpaRepository<CategoryEntity,Long> {


}
