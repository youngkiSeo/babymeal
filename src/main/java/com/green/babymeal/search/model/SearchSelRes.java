package com.green.babymeal.search.model;

import lombok.Data;

import java.util.List;

@Data
public class SearchSelRes {
    private int count;
    private int maxpage;
    private List<SearchSelproduct> dto;
}
