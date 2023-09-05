package com.green.babymeal.mypage;

import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.common.entity.SaleVolumnEntity;
import com.green.babymeal.mypage.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;


@Tag(name = "마이페이지")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {
    private final MypageServicelmpl service;

    @GetMapping("/orderlist")
    @Operation(summary = "주문내역조회",description = ""+
            "month: 조회하고싶은 기간(개월) <br>")
    public List<OrderlistSelVo> orderlist(@RequestParam int month){
       return service.orderlist(month);
    }

    @GetMapping("/orderlist/{orderCode}")
    @Operation(summary = "상세 주문내역",description = "")
    OrderlistDetailUserVo getOrderlistDetail(@PathVariable Long orderCode){
        return service.orderDetail(orderCode);
    }

    @DeleteMapping("/orderlist")
    @Operation(summary = "주문내역삭제",description = ""+
            "orderCode: 주문내역코드 <br>")
    public OrderlistEntity delorderlist(Long orderCode){
        return service.delorder(orderCode);
    }

    @GetMapping("/profile")
    @Operation(summary = "내 정보조회",description = ""+
            "image: 프로필이미지<br>"+
            "address: 주소 <br>")
    public ProfileVo getprofile(){
        return service.profile();
    }

    @PatchMapping("/profile")
    @Operation(summary = "내정보(유저 정보) 수정" , description = "")
    ProfileVo patchprofile(@RequestBody ProfileUpdDto dto){
        return service.profileupdate(dto);
    }

    @GetMapping("/profile/nickname")
    @Operation(summary = "닉네임 중복체크" ,
            description = "return : 0 이면 사용가능한 닉네임<br>" +
                    "return: 1 이면 중복된 닉네임입니다")
    public int getNickNamecheck(@RequestParam String nickname){
        return service.nicknmcheck(nickname);
    }

    @DeleteMapping("/profile")
    @Operation(summary = "회원탈퇴")
    public ResponseEntity<Integer> delprofile(HttpServletRequest req
            , HttpServletResponse res){
        service.deluser(req,res);
        return ResponseEntity.ok(1);
    }

    @PatchMapping(value = "/profile/pic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "유저 사진수정",description =
            "iuser : 회원의 고유값(PK) <- 해당 유저가 수정됨<br>"+
                    "pic : 사진 넣는 부분")
    public int patchPic(@RequestParam MultipartFile pic){
        //service.updPicUser(pic);
        return 1;
    }

    @PostMapping("/checkpw")
    @Operation(summary = "비밀번호체크",description = "return:1 이면 비밀번호 맞음," +
            "return:0이면 비밀번호 틀림")
    int checkPw(@RequestParam String upw){
        return service.selpw(upw);
    }


    @PostMapping("/salevolumn")
    public SaleVolumnEntity InsSaleVolumn(@RequestBody SaleVolumnDto dto){
        return service.Inssalevolumn(dto);
    }
    @GetMapping("/salevolum")
    @Operation(summary = "판매량 조회",description ="year:년도 ex)2022<br>"+
            "month: ex)01,02,03..11,12")
    public List<SaleVolumnVo> select(@RequestParam String year, @RequestParam String month){
        return service.Selectsale(year,month);
    }
}