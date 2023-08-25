package com.green.babymeal.auth;

import com.green.babymeal.auth.model.AuthResVo;
import com.green.babymeal.auth.model.SignInReqDto;
import com.green.babymeal.auth.model.SignUpReqDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResVo> postSignUp(@RequestBody SignUpReqDto dto
                                                , HttpServletRequest req
                                                , HttpServletResponse res) {
        AuthResVo vo = service.signUp(dto, req, res);
        return ResponseEntity.ok(vo);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResVo> postSignIn(@RequestBody SignInReqDto dto
                                                , HttpServletRequest req
                                                , HttpServletResponse res) {
        AuthResVo vo = service.signIn(dto, req, res);
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

}
