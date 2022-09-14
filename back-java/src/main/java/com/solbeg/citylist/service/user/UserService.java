package com.solbeg.citylist.service.user;

import com.solbeg.citylist.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> findUserByUsername(String username);
}
