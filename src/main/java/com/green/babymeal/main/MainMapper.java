package com.green.babymeal.main;

import com.green.babymeal.main.model.MainSelVo;
import com.green.babymeal.main.model.SelDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainMapper {
    int selMainCount();
    List<MainSelVo> selMainVo(int startIdx,int row);

    int birth(Long iuser);
    List<MainSelVo> birthRecommendFilter(int cateId,int row);

    List<MainSelVo> random();

    List<MainSelVo> bestSel();
    int bestSelAllCount();
    List<MainSelVo> bestSelAll(int startIdx,int row);

    Long levelSel(Long productId);

}
