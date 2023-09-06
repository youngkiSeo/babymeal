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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BabyService {
    private final BabyMapper mapper;
    private final BabyRepository babyRepository;
    private final BabyAlleRepository babyAlleRepository;
    private final AuthenticationFacade USERPK;
    private com.green.babymeal.common.entity.UserBabyinfoEntity UserBabyinfoEntity;

    public BabyInsVo insBaby(BabyInsDto dto){
        UserBabyinfoEntity entity = new UserBabyinfoEntity();
        UserEntity userEntity = new UserEntity();
        entity.setChildBirth(dto.getChildBirth());
        entity.setPrefer(dto.getPrefer());
        userEntity.setIuser(USERPK.getLoginUser().getIuser());
//        userEntity.setIuser(dto.getIuser());
        entity.setUserEntity(userEntity);
        babyRepository.save(entity);
        String ss= dto.getAllergyId();
        String[] split = ss.split(",");
        for (int i = 0; i < split.length; i++) {
            UserBabyalleEntity userBabyalleEntity = new UserBabyalleEntity();
            userBabyalleEntity.setUserBabyinfoEntity(entity);
            AllergyEntity allergyEntity = new AllergyEntity();
            allergyEntity.setAllergyId(Long.valueOf(split[i]));
            userBabyalleEntity.setAllergyEntity(allergyEntity);
            babyAlleRepository.save(userBabyalleEntity);
        }
        BabyInsVo vo = new BabyInsVo();
        vo.setChildBirth(dto.getChildBirth());
        vo.setPrefer(dto.getPrefer());
        vo.setAllegyId(dto.getAllergyId());
        vo.setIuser(USERPK.getLoginUser().getIuser());
//        vo.setIuser(dto.getIuser());
        return vo;
    }

    public int insBabyAllergy(BabyAllergyAddVo vo) {
        Long babyId = vo.getBabyId();
        Long allergyId = vo.getAllergyId();
        log.info(" 데이터 : {}", vo.getAllergyId());

        // babyId와 allergyId로 중복을 확인
        UserBabyalleEntity existingRecord = babyAlleRepository.findByBabyIdAndAllergyId(babyId, allergyId);

        if (existingRecord == null) {
            // 중복 데이터가 없으면
            UserBabyalleEntity newBabyAllergy = new UserBabyalleEntity();
            UserBabyinfoEntity userBabyinfoEntity = new UserBabyinfoEntity();
            userBabyinfoEntity.setBabyId(babyId);
            newBabyAllergy.setBabyallergy(vo.getAllergyId());
            newBabyAllergy.setUserBabyinfoEntity(userBabyinfoEntity);
            log.info(" 데이터2 : {}" , newBabyAllergy.getBabyallergy());
            log.info(" 데이터3 : {}" , newBabyAllergy.getUserBabyinfoEntity().getBabyId());

            babyAlleRepository.save(newBabyAllergy);
            return 1; // 삽입 성공
        }
        return 0; // 중복으로 인해 삽입 실패
    }

    public void delete(Long babyId){
//        repository.deleteById();
        babyRepository.deleteById(babyId);
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

    @Transactional
    public BabyInsVo update(BabyUpdDto dto){
        babyRepository.findByUserEntity_Iuser(USERPK.getLoginUser().getIuser());
//        listentity.listIterator().next(); // 인덱스값을 사용하지않아도 되게한다
        //iter // 포이치 자동으로 만들어줌
//        for (UserBabyinfoEntity entity : listentity) {
            UserBabyinfoEntity userBabyinfoEntity = new UserBabyinfoEntity();
            UserEntity userEntity = new UserEntity();
//            userBabyinfoEntity.setBabyId(dto.getBabyId());
            userBabyinfoEntity.setBabyId(dto.getBabyId());
            userBabyinfoEntity.setChildBirth(dto.getChildBirth());
            userBabyinfoEntity.setPrefer(dto.getPrefer());
            userEntity.setIuser(USERPK.getLoginUser().getIuser());
            userBabyinfoEntity.setUserEntity(userEntity);
            babyRepository.save(userBabyinfoEntity);


            String ss= dto.getAllergyId();
            String[] split = ss.split(",");
            for (int i = 0; i < split.length; i++) {
                UserBabyalleEntity userBabyalleEntity = new UserBabyalleEntity();
                userBabyalleEntity.setUserBabyinfoEntity(userBabyinfoEntity);
                AllergyEntity allergyEntity = new AllergyEntity();
                allergyEntity.setAllergyId(Long.valueOf(split[i]));
                userBabyalleEntity.setAllergyEntity(allergyEntity);
                babyAlleRepository.save(userBabyalleEntity);
            }
//        }
        BabyInsVo vo = new BabyInsVo();
        vo.setChildBirth(dto.getChildBirth());
        vo.setPrefer(dto.getPrefer());
        vo.setAllegyId(dto.getAllergyId());
        vo.setIuser(USERPK.getLoginUser().getIuser());
        return vo;
    }

    @Transactional
    public List updateBaby(BabyUpdDto dto){

        UserBabyinfoEntity userBabyinfoEntity = babyRepository.findById(dto.getBabyId()).get();
        userBabyinfoEntity.setPrefer(dto.getPrefer());
        userBabyinfoEntity.setChildBirth(dto.getChildBirth());
        UserBabyinfoEntity save = babyRepository.save(userBabyinfoEntity);

        BaByInfoVo vo=new BaByInfoVo();
        vo.setBabyId(save.getBabyId());
        vo.setPrefer(save.getPrefer());
        vo.setChildBirth(save.getChildBirth());


        babyAlleRepository.deleteByUserBabyinfoEntity_BabyId(dto.getBabyId());


        String[] split = dto.getAllergyId().split(",");


        for (int i = 0; i <  split.length; i++) {
            UserBabyalleEntity userBabyalleEntity=new UserBabyalleEntity();
            UserBabyinfoEntity userBabyinfo =new UserBabyinfoEntity();
            userBabyinfo.setBabyId(dto.getBabyId());
            AllergyEntity allergyEntity=new AllergyEntity();
            allergyEntity.setAllergyId(Long.valueOf(split[i]));
            userBabyalleEntity.setAllergyEntity(allergyEntity);
            userBabyalleEntity.setUserBabyinfoEntity(userBabyinfo);
            babyAlleRepository.save(userBabyalleEntity);
        }

        return null;
    }
}