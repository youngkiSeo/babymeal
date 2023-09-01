package com.green.babymeal.baby;

import com.green.babymeal.baby.model.*;
import com.green.babymeal.common.entity.AllergyEntity;
import com.green.babymeal.common.entity.UserBabyalleEntity;
import com.green.babymeal.common.entity.UserBabyinfoEntity;
import com.green.babymeal.common.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BabyService {

    private final BabyRepository rep;
    private final BabyAlleRepository repository;
    private final BabyMapper mapper;

    public BabyInsVo insBaby(BabyInsDto dto){


           UserBabyinfoEntity entity = new UserBabyinfoEntity();
           UserEntity userEntity = new UserEntity();
           entity.setBirthday(dto.getBirthday());
           entity.setPrefer(dto.getPrefer());
           userEntity.setIuser(dto.getIuser());
           entity.setUserEntity(userEntity);

//        if(rep.findById(dto.getIuser()) == null){
            rep.save(entity);
//        }
        String ss= dto.getAllegyId();
        String[] split = ss.split(",");

        for (int i = 0; i < split.length; i++) {
            UserBabyalleEntity userBabyalleEntity = new UserBabyalleEntity();
            userBabyalleEntity.setUserBabyinfoEntity(entity);
            AllergyEntity allergyEntity = new AllergyEntity();
            allergyEntity.setAllergyId(Long.valueOf(split[i]));
            userBabyalleEntity.setAllergyEntity(allergyEntity);
            repository.save(userBabyalleEntity);
        }




        /*BabyInsVo vo = new BabyInsVo();
        vo.setBirthday(entity.getBirthday());
                vo.setBirthday(entity.getBirthday());
                vo.setPrefer(entity.getPrefer());
                vo.setBabyId(userBabyalleEntity.getBabyallergy());
                *//*.birthday(entity.getBirthday())
                /*.prefer(entity.getPrefer())
                .iuser(userEntity.getIuser())
                //.babyId(userBabyalleEntity.getBabyallergy())
                .build();*/
        BabyInsVo vo = new BabyInsVo();
        vo.setBirthday(entity.getBirthday());
        vo.setBirthday(entity.getBirthday());
        vo.setPrefer(entity.getPrefer());
        vo.setAllegyId(dto.getAllegyId());
        return vo;

    }


    public void delete(Long babyId){
//        repository.deleteById();
        rep.deleteById(babyId);
    }

    public BabyInfoVo search(BabySelDto dto){
        List<BabyInsVo> babyInsVos = mapper.babyInfo(dto);
        log.info("{} : ", babyInsVos);
        List<BabyAlleDto> babyAlleList = new ArrayList<>();
        for (int i = 0; i < babyInsVos.size(); i++) {
            babyAlleList.add(mapper.babyAlle(babyInsVos.get(i).getBabyId()));
        }

        BabyInfoVo vo=new BabyInfoVo();
        vo.setList1(babyInsVos);
        vo.setList2(babyAlleList);
        return vo;


        /*UserEntity userEntity = new UserEntity();
        userEntity.setIuser(iuser);
        List<UserBabyinfoEntity> entity = rep.findByUserEntity_Iuser(iuser);

        return entity.stream().map(
                item -> BabySelVo.builder()
                        .prefer(item.getPrefer())
                        .birthday(item.getBirthday())
                        .iuser(item.getUserEntity()
                        .build()
        ).toList();*/

    }

}
