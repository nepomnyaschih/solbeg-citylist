package com.solbeg.citylist.repository;

import com.solbeg.citylist.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDTO, Long> {

    @Query("From UserDTO WHERE username=?1")
    Optional<UserDTO> findByUsername(String username);

}
