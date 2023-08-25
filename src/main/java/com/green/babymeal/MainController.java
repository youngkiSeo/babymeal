package com.green.babymeal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/oauth/redirect")
    public String getRedirect() {
        return "oauth/index";
    }

    @GetMapping("/user/signin")
    public String getUserSignin() {
        return "user/signin";
    }

}
