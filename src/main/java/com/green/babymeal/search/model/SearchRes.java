package com.green.babymeal.search.model;

import lombok.Data;

import java.util.List;

@Data
public class SearchRes {
    private String product;
    private int row;
    private int page;
    private int sorter;
    List<String> filter;
}
