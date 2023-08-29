package com.green.babymeal.baby;

import com.green.babymeal.baby.model.BabyInsDto;
import com.green.babymeal.baby.model.BabyInsVo;
import com.green.babymeal.common.entity.AllergyEntity;
import com.green.babymeal.common.entity.UserBabyalleEntity;
import com.green.babymeal.common.entity.UserBabyinfoEntity;
import com.green.babymeal.common.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BabyService {

    private final BabyRepository rep;
    private final BabyAlleRepository repository;

    public BabyInsVo insBaby(BabyInsDto dto){
        UserBabyinfoEntity entity = new UserBabyinfoEntity();
        entity.setBirthday(dto.getBirthday());
        entity.setPrefer(dto.getPrefer());

        UserEntity userEntity = new UserEntity();
        userEntity.setIuser(dto.getIuser());
        entity.setUserEntity(userEntity);


        List<UserBabyinfoEntity> byUserEntityIuser = rep.findByUserEntity_Iuser(dto.getIuser());

        if("null".equals(byUserEntityIuser)){
            rep.save(entity);
        }

            UserBabyalleEntity userBabyalleEntity = new UserBabyalleEntity();
            userBabyalleEntity.setUserBabyinfoEntity(entity);
            AllergyEntity allergyEntity = new AllergyEntity();
            allergyEntity.setAllergyId(dto.getAllegyId());
            userBabyalleEntity.setAllergyEntity(allergyEntity);
            repository.save(userBabyalleEntity);




        return BabyInsVo.builder()
                .birthday(entity.getBirthday())
                .prefer(entity.getPrefer())
                .iuser(userEntity.getIuser())
                .babyId(userBabyalleEntity.getBabyallergy())
                .build();
    }

    /*public void delete(Long babyId){
//        repository.deleteById();
        rep.deleteById(babyId);
    }*/

}
