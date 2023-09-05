package com.green.babymeal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SocialLogin {

    @GetMapping("/oauth/redirect")
    public String getRedirect() {
        return "oauth/index";
    }

    @GetMapping("/user/signin")
    public String getUserSignin() {
        return "user/signin";
    }

}
