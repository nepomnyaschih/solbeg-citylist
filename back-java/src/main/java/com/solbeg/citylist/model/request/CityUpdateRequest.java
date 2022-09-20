package com.solbeg.citylist.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
public class CityUpdateRequest {

    @NotBlank(message = "name can not be empty")
    private String name;

    @NotBlank(message = "photo can not be empty")
    private String photo;
}
