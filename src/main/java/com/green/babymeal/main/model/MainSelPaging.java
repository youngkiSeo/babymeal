package com.green.babymeal.main.model;

import lombok.Data;

import java.util.List;

@Data
public class MainSelPaging {

    private int maxPage;
    private int maxCount;
    private List<MainSelVo> list;
}
