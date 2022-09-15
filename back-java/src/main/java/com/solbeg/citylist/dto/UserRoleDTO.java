package com.solbeg.citylist.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
public class UserRoleDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String role;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDTO userDTO;

}
