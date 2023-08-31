package com.green.babymeal.main.model;

import com.green.babymeal.common.entity.ProductEntity;
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
    private int maxCount;
    private List<MainSelVo> list;
    private List<ProductEntity> entities;
}
