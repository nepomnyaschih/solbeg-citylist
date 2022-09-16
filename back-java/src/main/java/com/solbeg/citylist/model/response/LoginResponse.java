package com.solbeg.citylist.model.response;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {

    private String jwtToken;
    private List<String> roles;

}
