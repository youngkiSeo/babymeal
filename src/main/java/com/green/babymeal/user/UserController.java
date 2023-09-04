package com.green.babymeal.user;

import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.user.model.UserDelDto;
import com.green.babymeal.user.model.UserSelVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "관리자 페이지(유저 관련)")
public class UserController {

    private final UserService service;

    /*@PostMapping("/join")
    public ResponseEntity<UserEntity> PostUser(@RequestBody UserInsDto dto){
        return service.insUser(dto);
    }*/

    @GetMapping("/{uid}")
    @Operation(summary = "유저ID로 찾기",description = "ID : 회원의 아이디 입력<br>")
    public UserEntity GetUser(@PathVariable String uid){
        return service.selUser(uid);
    }

    @GetMapping("/search")
    @Operation(summary = "전체 유저보기",description = "※ 버튼을 누르면 전체유저가 나옴")
    public UserSelVo GetUserAll(@PageableDefault(direction = Sort.Direction.DESC) Pageable pageable){
        pageable.getSort();
        return service.selUserAll(pageable);
    }

    @DeleteMapping("/uid")
    @Operation(summary = "유저/관리자 삭제",description = "uid : 회원의 아이디 <- 해당 유저를 삭제한것처럼<br>")
    public ResponseEntity<Integer> delUser(@RequestParam String uid){
        service.delUser(uid);
        return ResponseEntity.ok(1);
    }

}

