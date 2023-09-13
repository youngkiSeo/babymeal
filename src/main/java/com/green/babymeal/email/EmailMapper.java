package com.green.babymeal.email;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface EmailMapper {
    List<String> selAllEmailAddress();
}
