package com.green.babymeal.baby;

import com.green.babymeal.baby.model.BaByInfoVo;
import com.green.babymeal.baby.model.BabyAllergyInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BabyMapper {

    List<BaByInfoVo> selBaby(Long iuser);
    List<BabyAllergyInfoVo> selBabyAllergy(Long babyId);
}
