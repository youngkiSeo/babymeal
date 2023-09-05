package com.green.babymeal.baby;

import com.green.babymeal.baby.model.*;
import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.AllergyEntity;
import com.green.babymeal.common.entity.UserBabyalleEntity;
import com.green.babymeal.common.entity.UserBabyinfoEntity;
import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.common.repository.BabyAlleRepository;
import com.green.babymeal.common.repository.BabyRepository;
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
    private final AuthenticationFacade USERPK;

    public BabyInsVo insBaby(BabyInsDto dto){

        // UserEntity에 유저pk 세팅
        UserEntity userEntity = new UserEntity();
        userEntity.setIuser(USERPK.getLoginUser().getIuser());
        // BabyInfo 선언 및 iuser 세팅
        UserBabyinfoEntity babyinfoEntity = new UserBabyinfoEntity();



        rep.save(babyinfoEntity);


        String ss= dto.getAllegyId();
        String[] split = ss.split(",");

        for (int i = 0; i < split.length; i++) {
            UserBabyalleEntity userBabyalleEntity = new UserBabyalleEntity();
            userBabyalleEntity.setUserBabyinfoEntity(babyinfoEntity);
            AllergyEntity allergyEntity = new AllergyEntity();
            allergyEntity.setAllergyId(Long.valueOf(split[i]));
            userBabyalleEntity.setAllergyEntity(allergyEntity);
            repository.save(userBabyalleEntity);
        }

        for (int i = 0; i < split.length; i++) {
            // allergyId로 이미 저장된 데이터가 있는지 확인
            Long allergyId = Long.valueOf(split[i]);
            UserBabyalleEntity existingUserBabyalle = repository.findByAllergyEntityAllergyIdAndUserBabyinfoEntity(allergyId, babyinfoEntity);
            if (existingUserBabyalle == null) {
                // 이미 저장된 데이터가 없으면 새로운 데이터를 저장
                // 중복있으면 저장하지 않음
                UserBabyalleEntity userBabyalleEntity = new UserBabyalleEntity();
                userBabyalleEntity.setUserBabyinfoEntity(babyinfoEntity);
                AllergyEntity allergyEntity = new AllergyEntity();
                allergyEntity.setAllergyId(allergyId);
                userBabyalleEntity.setAllergyEntity(allergyEntity);
                repository.save(userBabyalleEntity);
            }
        }

        BabyInsVo vo = new BabyInsVo();
        vo.setChildBirth(dto.getChildBirth());
        vo.setPrefer(dto.getPrefer());
        vo.setAllegyId(dto.getAllegyId());
        vo.setIuser(USERPK.getLoginUser().getIuser());
//        vo.setIuser(dto.getIuser());

        return vo;
    }


    public void delete(Long babyId){
//        repository.deleteById();
        rep.deleteById(babyId);
    }



    public List selBabyInfo(){
        List<BaByInfoVo> baByInfoVos = mapper.selBaby(USERPK.getLoginUser().getIuser()); //유저의 아기정보를 가져온다
        List list=new ArrayList(); //최종적으로 리턴 되는 리스트
        for (int i = 0; i < baByInfoVos.size(); i++) {
            BabyAllergyTotalVo vo=new BabyAllergyTotalVo();//아기정보와 아기알러지를 하나의 객체에 담는다
            BaByInfoVo baByInfoVo = baByInfoVos.get(i);
            Long babyId = baByInfoVos.get(i).getBabyId(); //아기의 PK값 가져옴
            List<BabyAllergyInfoVo> babyAllergyInfoVos = mapper.selBabyAllergy(babyId); //아기의 알러지 정보를 가져온다
            vo.setBaByInfoVo(baByInfoVo);
            vo.setBabyAllergyList(babyAllergyInfoVos);
            list.add(vo);
        }
        return list;
    }
}
