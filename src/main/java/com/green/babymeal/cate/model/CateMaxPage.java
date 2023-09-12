package com.green.babymeal.cate.model;

import lombok.Data;

import java.util.List;

@Data
public class CateMaxPage {

    private int maxPage;
    private Long count;
    private List<CateSelVo> list;
}
