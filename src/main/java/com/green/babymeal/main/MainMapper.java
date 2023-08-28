package com.green.babymeal.main;

import com.green.babymeal.main.model.MainSelVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainMapper {
    int SelMainCount();
    List<MainSelVo> mainSel();

}
