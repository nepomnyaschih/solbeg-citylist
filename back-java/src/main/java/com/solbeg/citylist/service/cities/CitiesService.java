package com.solbeg.citylist.service.cities;

import com.solbeg.citylist.model.City;
import com.solbeg.citylist.model.request.CityUpdateRequest;
import org.springframework.data.domain.Page;

public interface CitiesService {

    Page<City> findPaginated(String searchText, Integer page, Integer size);

    boolean updateCity(Long id, CityUpdateRequest updateRequest);
}
