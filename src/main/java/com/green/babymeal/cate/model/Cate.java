package com.green.babymeal.cate.model;

import lombok.Data;

import java.util.List;

@Data
public class Cate {
    private Category category;
    private List<CateViewRepositoryDetail> cateDetail;
}
