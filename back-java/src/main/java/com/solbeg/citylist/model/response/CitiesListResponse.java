package com.solbeg.citylist.model.response;

import com.solbeg.citylist.model.City;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CitiesListResponse {

    private List<City> cities;
    private Long totalCount;
    private Integer totalPages;

}
