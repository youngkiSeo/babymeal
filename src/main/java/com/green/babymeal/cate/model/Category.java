package com.green.babymeal.cate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Category {
    private Long cateId;
    private String cateName;


}
