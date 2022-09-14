package com.solbeg.citylist.security;

import com.solbeg.citylist.dto.UserDTO;
import com.solbeg.citylist.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class MyUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDTO> userInDb = userRepository.findByUsername(username);
        UserDTO userDTO = userInDb.orElseThrow(() -> new UsernameNotFoundException("Not Found in DB"));
        return new SecureUser(userDTO);
    }
}
