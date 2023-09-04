package com.green.babymeal.cate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class Cate {
    private Category category;
    private List<CateViewRepositoryDetail> cateDetail;
}
