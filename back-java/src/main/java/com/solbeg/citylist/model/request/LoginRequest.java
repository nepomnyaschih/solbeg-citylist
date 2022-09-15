package com.solbeg.citylist.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class LoginRequest {

    @NotBlank(message = "Username can not be empty")
    private String username;

    @NotBlank(message = "Password can not be empty")
    private String password;

}
