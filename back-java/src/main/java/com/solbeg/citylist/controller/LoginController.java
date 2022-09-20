package com.solbeg.citylist.controller;

import com.solbeg.citylist.exceptions.LoginException;
import com.solbeg.citylist.model.request.LoginRequest;
import com.solbeg.citylist.model.response.LoginResponse;
import com.solbeg.citylist.service.login.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> doLogin(@RequestBody @Valid LoginRequest loginRequest) throws LoginException {
        var loginResponse = loginService.doLogin(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<LoginResponse> handleLoginException(LoginException e) {
        return new ResponseEntity<>(
                LoginResponse.builder()
                        .isSuccess(false)
                        .build(),
                new HttpHeaders(),
                HttpStatus.FORBIDDEN
        );
    }
}
