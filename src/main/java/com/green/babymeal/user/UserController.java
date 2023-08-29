package com.green.babymeal.user;

import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.user.model.UserDelDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    /*@PostMapping("/join")
    public ResponseEntity<UserEntity> PostUser(@RequestBody UserInsDto dto){
        return service.insUser(dto);
    }*/

    @GetMapping("/{uid}")
    public UserEntity GetUser(@PathVariable String uid){
        return service.selUser(uid);
    }

    @GetMapping("/search")
    public List<UserEntity> GetUserAll(){
        return service.selUserAll();
    }

    @DeleteMapping("/uid")
    @Operation(summary = "유저/관리자 삭제",description = "iuser : 회원의 고유값(PK) <- 해당 유저가 삭제됨<br>")
    public ResponseEntity<Integer> delUser(@RequestParam String uid){
        service.delUser(uid);
        return ResponseEntity.ok(1);
    }

}

