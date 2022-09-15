package com.solbeg.citylist.service.user;

import com.solbeg.citylist.dto.UserDTO;
import com.solbeg.citylist.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<UserDTO> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
