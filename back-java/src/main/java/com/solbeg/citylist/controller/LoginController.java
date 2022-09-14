package com.solbeg.citylist.controller;

import com.solbeg.citylist.request.LoginRequest;
import com.solbeg.citylist.response.LoginResponse;
import com.solbeg.citylist.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LoginController {
    private final LoginService loginService;

    public LoginController(@Autowired LoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = loginService.doLogin(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
