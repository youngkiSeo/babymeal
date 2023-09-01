package com.green.babymeal.admin.model;

import lombok.Data;

@Data
public class AdminProductEntity {
    String title;
    String name;
    int price;
    int quantity;
    String description;
}
