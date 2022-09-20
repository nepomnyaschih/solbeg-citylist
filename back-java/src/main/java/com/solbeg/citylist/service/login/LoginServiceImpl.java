package com.solbeg.citylist.service.login;

import com.solbeg.citylist.exceptions.LoginException;
import com.solbeg.citylist.model.request.LoginRequest;
import com.solbeg.citylist.model.response.LoginResponse;
import com.solbeg.citylist.security.SecureUser;
import com.solbeg.citylist.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse doLogin(LoginRequest loginRequest) throws LoginException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            SecureUser userDetails = (SecureUser) authentication.getPrincipal();
            String jwtToken = JwtUtil.generateJwtToken(userDetails.getUsername());
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            return LoginResponse.builder()
                    .jwtToken(jwtToken)
                    .roles(roles)
                    .build();
        } catch (AuthenticationException e) {
            throw new LoginException(e.getMessage());
        }
    }
}
