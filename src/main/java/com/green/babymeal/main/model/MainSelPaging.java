package com.green.babymeal.main.model;

import com.green.babymeal.common.entity.ProductEntity;
import com.querydsl.core.Tuple;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MainSelPaging {

    private int maxPage;
    private Long maxCount;
    private List<MainSelVo> list;
}
