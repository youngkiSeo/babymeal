package com.green.babymeal.user;

import com.green.babymeal.common.config.security.model.ProviderType;
import com.green.babymeal.common.config.security.model.RoleType;
import com.green.babymeal.common.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByProviderTypeAndUid(ProviderType providerType, String uid);
    UserEntity findByRoleTypeAndUid(RoleType roleType, String uid);
    UserEntity findByUid(String uid);
    //UserBabyinfoEntity findByGenderTypeAndGenger(GenderType genderType, Character gender);

    //닉네임찾기
    UserEntity findByNickNm(String nickNm);

}
