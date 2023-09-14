package com.green.babymeal.admin.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderSelVo {
    private int page;
    private int count;
    private int maxPage;
    private int Allcount;
    private List list;
}
