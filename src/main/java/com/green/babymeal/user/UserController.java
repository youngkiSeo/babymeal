package com.green.babymeal.user;

import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.user.model.UserInsDto;
import com.green.babymeal.user.model.UserSelDto;
import com.green.babymeal.user.model.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/join")
    public ResponseEntity<UserEntity> PostUser(@RequestBody UserInsDto dto){
        return service.insUser(dto);
    }

    @GetMapping("/{uid}")
    public UserEntity GetUser(@PathVariable String uid){
        return service.selUser(uid);
    }

    @GetMapping("/search")
    public List<UserEntity> GetUserAll(){
        return service.selUserAll();
    }

}

