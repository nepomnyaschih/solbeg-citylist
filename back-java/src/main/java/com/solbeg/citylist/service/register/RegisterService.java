package com.solbeg.citylist.service.register;

import com.solbeg.citylist.model.request.RegisterRequest;
import com.solbeg.citylist.model.response.RegisterResponse;

public interface RegisterService {
    RegisterResponse doRegister(RegisterRequest registerRequest);

}
