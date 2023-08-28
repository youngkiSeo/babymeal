package com.green.babymeal.main;

import com.green.babymeal.main.model.MainSelVo;
import com.green.babymeal.main.model.SelDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainMapper {
    int SelMainCount();
    List<MainSelVo> SelMainVo(int startIdx,int row);

    int birth(Long iuser);
    List<MainSelVo> birthRecommendFilter(int cateId,int row);

}
