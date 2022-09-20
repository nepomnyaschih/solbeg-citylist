package com.solbeg.citylist.controller;

import com.solbeg.citylist.model.request.RegisterRequest;
import com.solbeg.citylist.model.response.RegisterResponse;
import com.solbeg.citylist.service.register.RegisterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<?> doRegister(@RequestBody @Valid RegisterRequest registerRequest) {
        RegisterResponse registerResponse = registerService.doRegister(registerRequest);
        return ResponseEntity.ok(registerResponse);
    }
}
