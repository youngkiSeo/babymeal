package com.green.babymeal.main.model;

import lombok.Data;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;


@Data
public class SelDto {

    private int check;
    private int page;
    private int row;
}
