package com.solbeg.citylist.controller;

import com.solbeg.citylist.model.request.LoginRequest;
import com.solbeg.citylist.model.response.LoginResponse;
import com.solbeg.citylist.service.login.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = loginService.doLogin(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
