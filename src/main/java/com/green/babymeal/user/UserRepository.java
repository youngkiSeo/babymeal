package com.green.babymeal.user;

import com.green.babymeal.common.config.security.model.GenderType;
import com.green.babymeal.common.config.security.model.ProviderType;
import com.green.babymeal.common.entity.UserBabyinfoEntity;
import com.green.babymeal.common.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByProviderTypeAndEmail(ProviderType providerType, String email);
    //UserBabyinfoEntity findByGenderTypeAndGenger(GenderType genderType, Character gender);
}
