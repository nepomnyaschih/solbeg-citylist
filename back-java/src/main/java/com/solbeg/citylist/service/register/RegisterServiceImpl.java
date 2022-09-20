package com.solbeg.citylist.service.register;

import com.solbeg.citylist.dto.UserDTO;
import com.solbeg.citylist.dto.UserRoleDTO;
import com.solbeg.citylist.model.UserRole;
import com.solbeg.citylist.repository.RegisterRepository;
import com.solbeg.citylist.model.request.RegisterRequest;
import com.solbeg.citylist.model.response.RegisterResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.solbeg.citylist.model.UserRole.ROLE_ALLOW_EDIT;
import static com.solbeg.citylist.model.UserRole.ROLE_BASIC;

@Service
@AllArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private static final String USER_HAS_IN_DB_MSG = "User has already in db";
    private static final String USER_WAS_SAVED_MSG = "User was saved";

    private final PasswordEncoder passwordEncoder;
    private final RegisterRepository registerRepository;

    @Transactional
    @Override
    public RegisterResponse doRegister(RegisterRequest registerRequest) {
        Optional<UserDTO> isUserInDb = registerRepository.findByUsername(registerRequest.getUsername());
        if (isUserInDb.isPresent()) {
            return RegisterResponse.builder()
                    .isRegistered(true)
                    .message(USER_HAS_IN_DB_MSG)
                    .build();
        }

        UserDTO newUser = mapNewRegisterToNewUser(registerRequest);
        registerRepository.save(newUser);

        return RegisterResponse.builder()
                .isRegistered(true)
                .message(USER_WAS_SAVED_MSG)
                .build();
    }

    private UserDTO mapNewRegisterToNewUser(RegisterRequest registerRequest) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(registerRequest.getUsername());
        userDTO.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        UserRoleDTO basicRole = new UserRoleDTO();
        basicRole.setRole(ROLE_BASIC.name());
        basicRole.setUserDTO(userDTO);
        userDTO.getUserRoles().add(basicRole);

        if (registerRequest.getCanEdit()) {
            UserRoleDTO editRole = new UserRoleDTO();
            editRole.setRole(ROLE_ALLOW_EDIT.name());
            editRole.setUserDTO(userDTO);
            userDTO.getUserRoles().add(editRole);
        }

        return userDTO;
    }
}
