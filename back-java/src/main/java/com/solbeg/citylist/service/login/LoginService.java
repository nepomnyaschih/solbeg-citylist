package com.solbeg.citylist.service.login;

import com.solbeg.citylist.request.LoginRequest;
import com.solbeg.citylist.response.LoginResponse;

public interface LoginService {

    LoginResponse doLogin(LoginRequest loginRequest);
}
