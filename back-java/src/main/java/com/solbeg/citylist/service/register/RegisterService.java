package com.solbeg.citylist.service.register;

import com.solbeg.citylist.request.RegisterRequest;
import com.solbeg.citylist.response.RegisterResponse;

public interface RegisterService {
    RegisterResponse doRegister(RegisterRequest registerRequest);

}
