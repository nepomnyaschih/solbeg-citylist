package com.solbeg.citylist.service.cities;

import com.solbeg.citylist.model.City;
import com.solbeg.citylist.model.request.CityUpdateRequest;
import com.solbeg.citylist.model.response.CityUpdateResponse;
import org.springframework.data.domain.Page;

public interface CitiesService {

    Page<City> findPaginated(String searchText, Integer page, Integer size);

    CityUpdateResponse updateCity(Long id, CityUpdateRequest updateRequest);
}
