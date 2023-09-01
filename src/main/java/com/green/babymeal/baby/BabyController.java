package com.green.babymeal.baby;

import com.green.babymeal.baby.model.BabyInsDto;
import com.green.babymeal.baby.model.BabyInsVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/baby")
@RequiredArgsConstructor
public class BabyController {

    private final BabyService service;

    @PostMapping
    public BabyInsVo postBaby(@RequestBody BabyInsDto dto){
        return service.insBaby(dto);
    }

    @DeleteMapping("/{babyId}")
    public Integer deleteBaby(@RequestParam Long babyId){
        service.delete(babyId);
        return 1;
    }

    @GetMapping
    public List selBaby(){
        return service.selBabyInfo();
    }
}
