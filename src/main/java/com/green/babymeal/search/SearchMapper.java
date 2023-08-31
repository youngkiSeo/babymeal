package com.green.babymeal.search;

import com.green.babymeal.search.model.SearchSelDto;
import com.green.babymeal.search.model.SearchSelVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchMapper {
    List<SearchSelVo> selfilter(SearchSelDto dto);
    int maxpage(String word,String msg ,String allergy);
}
