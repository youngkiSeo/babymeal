package com.green.babymeal.auth;

import com.green.babymeal.auth.model.AuthDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/sign-in")
    public ResponseEntity postSignIn(
            HttpServletRequest req,
            HttpServletResponse res,
            @RequestBody AuthDto authDto) {
        return service.signIn(req, res, authDto);
    }
}
