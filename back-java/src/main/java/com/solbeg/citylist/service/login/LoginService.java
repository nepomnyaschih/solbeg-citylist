package com.solbeg.citylist.service.login;

import com.solbeg.citylist.model.request.LoginRequest;
import com.solbeg.citylist.model.response.LoginResponse;

public interface LoginService {

    LoginResponse doLogin(LoginRequest loginRequest);
}
