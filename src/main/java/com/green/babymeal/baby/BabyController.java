package com.green.babymeal.baby;

import com.green.babymeal.baby.model.BabyInsDto;
import com.green.babymeal.baby.model.BabyInsVo;
import com.green.babymeal.common.entity.UserBabyinfoEntity;
import com.green.babymeal.common.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/baby")
@RequiredArgsConstructor
public class BabyController {

    private final BabyService service;

    @PostMapping
    public BabyInsVo postUser(@RequestBody BabyInsDto dto){
        return service.insBaby(dto);
    }
}
