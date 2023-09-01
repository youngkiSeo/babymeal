package com.green.babymeal.baby;

import com.green.babymeal.baby.model.*;
import com.green.babymeal.common.entity.AllergyEntity;
import com.green.babymeal.common.entity.BabyalleEntity;
import com.green.babymeal.common.entity.BabyinfoEntity;
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

    private final BabyMapper mapper;
    private final BabyRepository rep;
    private final BabyAlleRepository repository;

    public BabyInsVo insBaby(BabyInsDto dto){


           BabyinfoEntity entity = new BabyinfoEntity();
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
            BabyalleEntity babyalleEntity = new BabyalleEntity();
            babyalleEntity.setBabyinfoEntity(entity);
            AllergyEntity allergyEntity = new AllergyEntity();
            allergyEntity.setAllergyId(Long.valueOf(split[i]));
            babyalleEntity.setAllergyEntity(allergyEntity);
            repository.save(babyalleEntity);
        }

        BabyInsVo vo = new BabyInsVo();
        vo.setBirthday(dto.getBirthday());
        vo.setPrefer(dto.getPrefer());
        vo.setAllegyId(dto.getAllegyId());
        vo.setIuser(dto.getIuser());

        return vo;
    }


    public void delete(Long babyId){
//        repository.deleteById();
        rep.deleteById(babyId);
    }



    public List sel(Long iuser){
        List<BaByInfoVo> baByInfoVos = mapper.selBaby(iuser);
        List list=new ArrayList();
        for (int i = 0; i < baByInfoVos.size(); i++) {
            BabyAllergyTotalVo vo=new BabyAllergyTotalVo();
            BaByInfoVo baByInfoVo = baByInfoVos.get(i);
            Long babyId = baByInfoVos.get(i).getBabyId();
            List<BabyAllergyInfoVo> babyAllergyInfoVos = mapper.selBabyAllergy(babyId);
            vo.setBaByInfoVo(baByInfoVo);
            vo.setBabyAllergyList(babyAllergyInfoVos);
            list.add(vo);
        }
        return list;

    }
}
