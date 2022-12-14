package com.solbeg.citylist.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginResponse {

    private boolean isSuccess;
    private String jwtToken;
    private List<String> roles;

}
