package com.green.babymeal.search.model;

import lombok.Data;

@Data
public class SearchSelDto {
    private String word;
    private String msg;
    private int startIdx;
    private int page;
    private int row;
    private int sorter;
    private String allergy;
}
