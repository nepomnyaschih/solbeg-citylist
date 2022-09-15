package com.solbeg.citylist.service.cities;

import com.solbeg.citylist.model.City;
import org.springframework.data.domain.Page;

public interface CitiesService {

    Page<City> findPaginated(String searchText, Integer page, Integer size);
}
