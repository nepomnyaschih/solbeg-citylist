package com.solbeg.citylist.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private boolean isRegistered;
    private String message;

}
