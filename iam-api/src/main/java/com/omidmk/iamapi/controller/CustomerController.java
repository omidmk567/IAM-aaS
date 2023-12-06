package com.omidmk.iamapi.controller;

import com.omidmk.iamapi.oauth2.model.Deployment;
import com.omidmk.iamapi.oauth2.model.IAMUser;
import com.omidmk.iamapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/deployments")
    @ResponseStatus(HttpStatus.CREATED)
    public void createDeployment(@AuthenticationPrincipal IAMUser user) {

    }

    @GetMapping("/deployments")
    public List<Deployment> getDeployments(@AuthenticationPrincipal IAMUser user) {
        return null;
    }

    @GetMapping("/userinfo")
    public IAMUser getUserInfo(@AuthenticationPrincipal IAMUser user) {
        return user;
    }
}
