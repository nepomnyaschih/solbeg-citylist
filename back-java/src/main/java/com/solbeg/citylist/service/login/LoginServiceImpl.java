package com.solbeg.citylist.service.login;

import com.solbeg.citylist.request.LoginRequest;
import com.solbeg.citylist.response.LoginResponse;
import com.solbeg.citylist.security.SecureUser;
import com.solbeg.citylist.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;

    public LoginServiceImpl(@Autowired AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public LoginResponse doLogin(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        SecureUser userDetails = (SecureUser) authentication.getPrincipal();
        String jwtToken = JwtUtil.generateJwtToken(userDetails.getUsername());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setJwtToken(jwtToken);
        return loginResponse;
    }
}
