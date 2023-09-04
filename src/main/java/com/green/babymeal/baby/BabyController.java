package com.green.babymeal.baby;

import com.green.babymeal.baby.model.BabyInsDto;
import com.green.babymeal.baby.model.BabyInsVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/baby")
@Tag(name = "아기 페이지")
@RequiredArgsConstructor
public class BabyController {

    private final BabyService service;

    @PostMapping
    @Operation(summary = "아기정보 등록",description = "" +
            "childBirth : 아기의 생년월일<br>" +
            "prefer : 기호식품<br>" +
            "allegyId : 아이의 알러지 정보( , 로 구분을 해야 합니다.) (ex : 1,2,3,4)<br>" +
            "※ 로그인 해야 올릴수있습니다.")
    public BabyInsVo postBaby(@RequestBody BabyInsDto dto){
        return service.insBaby(dto);
    }

    @DeleteMapping("/{babyId}")
    @Operation(summary = "아기정보 삭제",description = "" +
            "babyId : 아기의 PK값")
    public Integer deleteBaby(@RequestParam Long babyId){
        service.delete(babyId);
        return 1;
    }

    @GetMapping
    @Operation(summary = "아기정보 보기",description = "" +
            "※ 로그인 해야 볼수있습니다.")
    public List selBaby(){
        return service.selBabyInfo();
    }

}
