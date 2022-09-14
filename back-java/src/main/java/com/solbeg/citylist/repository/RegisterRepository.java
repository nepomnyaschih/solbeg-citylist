package com.solbeg.citylist.repository;

import com.solbeg.citylist.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RegisterRepository extends JpaRepository<UserDTO, Long> {

    @Query("FROM UserDTO WHERE username=?1")
    Optional<UserDTO> findByUsername(String username);
}
