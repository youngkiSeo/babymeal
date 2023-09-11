package com.green.babymeal.admin.model;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdminProductUpdDto {
    Long productId;
    String name;
    int price;
    int quantity;
    String description;
    int saleVolume;
    int category;
    float pointRate;

    private List<Long> allergyId = new ArrayList<>(); // 기본값으로 빈 리스트를 설정
    List<Integer> cateDetail;

}
