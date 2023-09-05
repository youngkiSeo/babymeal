package com.green.babymeal.kakaopay;




import com.green.babymeal.kakaopay.model.KakaoPayDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KakaoMapper {

    Long insOrderList(KakaoPayDto dto);
}
