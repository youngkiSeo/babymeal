package com.green.babymeal.search;

import com.green.babymeal.search.model.SearchSelDto;
import com.green.babymeal.search.model.SearchSelProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchMapper {
    List<SearchSelProductDto> selfilter(SearchSelDto dto);
    int maxpage(String word,String msg ,String allergy);
}
