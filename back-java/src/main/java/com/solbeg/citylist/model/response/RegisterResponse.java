package com.solbeg.citylist.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class RegisterResponse {

    private boolean isRegistered;
    private String message;

}
