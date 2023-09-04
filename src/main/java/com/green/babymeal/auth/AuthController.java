package com.green.babymeal.auth;

import com.green.babymeal.auth.model.AuthResVo;
import com.green.babymeal.auth.model.SignInReqDto;
import com.green.babymeal.auth.model.SignUpReqDto;
import com.green.babymeal.auth.model.SignUpResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "회원 페이지")
public class AuthController {
    private final AuthService service;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입",
            description = "가입하려는 유저의 정보를 넣으면 됩니다.<br>" +
                    "※ 가입 성공시 \"가입에 성공하셨어요\" 메시지 나옴")
    public ResponseEntity<SignUpResultDto> postSignUp(@RequestBody SignUpReqDto dto
            , HttpServletRequest req
            , HttpServletResponse res) {

        SignUpResultDto dto1 = service.signUp(dto, req, res);
        //AuthResVo vo = service.signUp(dto, req, res);
        //return ResponseEntity.ok(vo);
        return ResponseEntity.ok(dto1);
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로그인",
            description = "해당유저의 아이디 , 비밀번호를 넣으면 됩니다.<br>" +
                    "※ 소셜때문에 이메일에서 아이디 방식으로 바뀜")
    public ResponseEntity<AuthResVo> postSignIn(@RequestBody SignInReqDto dto
            , HttpServletRequest req
            , HttpServletResponse res) {
        AuthResVo vo = service.signIn(dto, req, res);
        return ResponseEntity.ok(vo);
    }

    @PostMapping("/admin/sign-in")
    @Operation(summary = "관리자 로그인",
            description = "해당유저의 아이디 , 비밀번호를 넣으면 됩니다.<br>" +
                    "※ 소셜때문에 이메일에서 아이디 방식으로 바뀜")
    public ResponseEntity<AuthResVo> adminPostSignIn(@RequestBody SignInReqDto dto
            , HttpServletRequest req
            , HttpServletResponse res) {
        AuthResVo vo = service.adminSignIn(dto, req, res);
        return ResponseEntity.ok(vo);
    }

    @GetMapping("/sign-out")
    @Operation(summary = "로그아웃",
            description = "해당유저 로그아웃<br>" +
                    "※ 리프레시 토큰을 바로 삭제를 한다음 엑세스토큰은 블랙리스트에 추가함")
    public ResponseEntity getSignout(HttpServletRequest req
            , HttpServletResponse res) {
        service.signOut(req, res);
//        ResponseCookie responseCookie = ResponseCookie.from("access-token");
        return ResponseEntity.ok(1);
    }

    /*@GetMapping("/sign-out")
    @Operation(summary = "로그아웃",
            description = "엑세스토큰을 넣으면 해당유저가 로그아웃<br>" +
                    "※ 소셜때문에 이렇게 한것으로 알고있어요")
    public ResponseEntity getSignout(@RequestParam(required = false) String accessToken
            , HttpServletRequest req
            , HttpServletResponse res) {
        service.signOut(accessToken, req, res);
        return ResponseEntity.ok(1);
    }*/

    @GetMapping("/refresh")
    @Operation(summary = "엑세스 토큰 재발급",
            description = "버튼을 누를 시 바로 됨<br>" +
                            "※ 로그인 해야 되요")
    public ResponseEntity<AuthResVo> getRefresh(HttpServletRequest req) {
        AuthResVo vo = service.refresh(req);
        return ResponseEntity.ok(vo);
    }


    // -----------------비밀번호 찾기

    @PostMapping("/id")
    @Operation(summary = "아이디 중복체크",
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
