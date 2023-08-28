package com.green.babymeal.search.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchPopularVo {
    private String product;
    private Double count;
}
