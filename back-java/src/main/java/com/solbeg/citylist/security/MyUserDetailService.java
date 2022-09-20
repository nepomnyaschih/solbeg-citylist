package com.solbeg.citylist.security;

import com.solbeg.citylist.dto.UserDTO;
import com.solbeg.citylist.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@AllArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDTO> userInDb = userRepository.findByUsername(username);
        UserDTO userDTO = userInDb.orElseThrow(() -> new UsernameNotFoundException("Not Found in DB"));
        return new SecureUser(userDTO);
    }
}
