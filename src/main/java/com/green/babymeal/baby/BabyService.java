package com.green.babymeal.baby;

import com.green.babymeal.baby.model.BabyInsDto;
import com.green.babymeal.baby.model.BabyInsVo;
import com.green.babymeal.common.entity.UserBabyalleEntity;
import com.green.babymeal.common.entity.UserBabyinfoEntity;
import com.green.babymeal.common.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

        UserEntity entity1 = new UserEntity();
        entity1.setIuser(dto.getIuser());
        entity.setUserEntity(entity1);
        rep.save(entity);

        UserBabyalleEntity userBabyalleEntity = new UserBabyalleEntity();
        userBabyalleEntity.setUserBabyinfoEntity(entity);
        repository.save(userBabyalleEntity);

        return BabyInsVo.builder()
                .birthday(entity.getBirthday())
                .prefer(entity.getPrefer())
                .iuser(entity1.getIuser())
                .babyId(userBabyalleEntity.getBabyallergy())
                .build();
    }
}
