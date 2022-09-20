package com.solbeg.citylist.model.response;

import com.solbeg.citylist.model.City;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CitiesListResponse {

    private List<City> cities;
    private Long totalCount;
    private Integer totalPages;

}
