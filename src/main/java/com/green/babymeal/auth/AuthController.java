package com.green.babymeal.auth;

import com.green.babymeal.auth.model.AuthResVo;
import com.green.babymeal.auth.model.SignInReqDto;
import com.green.babymeal.auth.model.SignUpReqDto;
import com.green.babymeal.auth.model.SignUpResultDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign-api")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService service;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResultDto> postSignUp(@RequestBody SignUpReqDto dto
            , HttpServletRequest req
            , HttpServletResponse res) {

        SignUpResultDto dto1 = service.signUp(dto, req, res);
        //AuthResVo vo = service.signUp(dto, req, res);
        //return ResponseEntity.ok(vo);
        return ResponseEntity.ok(dto1);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResVo> postSignIn(@RequestBody SignInReqDto dto
            , HttpServletRequest req
            , HttpServletResponse res) {
        AuthResVo vo = service.signIn(dto, req, res);
        return ResponseEntity.ok(vo);
    }

    @PostMapping("/admin/sign-in")
    public ResponseEntity<AuthResVo> adminPostSignIn(@RequestBody SignInReqDto dto
            , HttpServletRequest req
            , HttpServletResponse res) {
        AuthResVo vo = service.adminSignIn(dto, req, res);
        return ResponseEntity.ok(vo);
    }

    @GetMapping("/sign-out")
    public ResponseEntity getSignout(@RequestParam(required = false) String accessToken
            , HttpServletRequest req
            , HttpServletResponse res) {
        service.signOut(accessToken, req, res);
        return ResponseEntity.ok(1);
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthResVo> getRefresh(HttpServletRequest req) {
        AuthResVo vo = service.refresh(req);
        return ResponseEntity.ok(vo);
    }


    // -----------------비밀번호 찾기

    @PostMapping("/id")
    @Operation(summary = "id 중복체크",
            description = "return : 1이면 중복인것")
    public int emailCheck(@RequestParam String uid){
        return service.uidCheck(uid);
    }
    @GetMapping("/nickname")
    @Operation(summary = "닉네임 중복체크" ,
            description = "return : 1이면 중복인것")
    int getNickNamecheck(@RequestParam String nickname){
        return service.nicknmcheck(nickname);
    }

}
