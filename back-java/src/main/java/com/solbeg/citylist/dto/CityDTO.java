package com.solbeg.citylist.dto;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "cities")
@Data
public class CityDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String photo;

}
