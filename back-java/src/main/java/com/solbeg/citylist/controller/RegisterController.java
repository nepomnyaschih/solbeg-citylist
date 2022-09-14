package com.solbeg.citylist.controller;

import com.solbeg.citylist.request.RegisterRequest;
import com.solbeg.citylist.response.RegisterResponse;
import com.solbeg.citylist.service.register.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(@Autowired RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> doRegister(@RequestBody @Valid RegisterRequest registerRequest) {
        RegisterResponse registerResponse = registerService.doRegister(registerRequest);
        return ResponseEntity.ok(registerResponse);
    }
}
