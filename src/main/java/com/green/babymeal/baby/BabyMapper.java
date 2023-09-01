package com.green.babymeal.baby;

import com.green.babymeal.baby.model.BabyAlleDto;
import com.green.babymeal.baby.model.BabyInfoVo;
import com.green.babymeal.baby.model.BabyInsVo;
import com.green.babymeal.baby.model.BabySelDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BabyMapper {
    List<BabyInsVo> babyInfo(BabySelDto dto);
    BabyAlleDto babyAlle(Long babyId);
}
