package com.solbeg.citylist.service.cities;

import com.solbeg.citylist.dto.CityDTO;
import com.solbeg.citylist.mappers.CitiesDtoMapper;
import com.solbeg.citylist.model.City;
import com.solbeg.citylist.repository.CitiesRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CitiesServiceImpl implements CitiesService {

    private final CitiesRepository citiesRepository;
    private final CitiesDtoMapper dtoMapper = Mappers.getMapper(CitiesDtoMapper.class);

    @Override
    public Page<City> findPaginated(String searchText, Integer page, Integer size) {
        var dtoPage = citiesRepository.findByNameLikeIgnoreCase("%"+searchText+"%", PageRequest.of(page, size));
        return dtoPage.map(dtoMapper::dtoToDomain);
    }
}
