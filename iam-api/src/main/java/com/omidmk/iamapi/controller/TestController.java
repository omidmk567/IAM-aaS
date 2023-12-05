package com.omidmk.iamapi.controller;

import com.omidmk.iamapi.oauth2.model.IAMUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/v1/test")
    public String test(@AuthenticationPrincipal IAMUser iamUser) {
        return iamUser.getEmail();
    }
}
