package com.green.babymeal.main.model;

import lombok.Builder;
import lombok.Data;

@Data

public class MainSelVo {


    private Long productId;
    private String thumbnail;
    private String name;
    private int price;
    private int quantity;
    private int saleVoumn;
    private float pointRate;

}
