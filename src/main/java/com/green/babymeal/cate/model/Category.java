package com.green.babymeal.cate.model;

import lombok.Data;

import java.util.List;

@Data
public class Category {
    private Long CateId;
    private String CateName;
    private List<CateViewRepositoryDetail> cateDetail;
}
