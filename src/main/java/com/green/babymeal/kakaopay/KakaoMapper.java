package com.green.babymeal.kakaopay;




import com.green.babymeal.kakaopay.model.KakaoPayDto;
import com.green.babymeal.kakaopay.model.KakaoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface KakaoMapper {

    Long insOrderList(KakaoPayDto dto);
}
