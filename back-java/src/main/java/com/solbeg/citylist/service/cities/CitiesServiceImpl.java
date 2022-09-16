package com.solbeg.citylist.service.cities;

import com.solbeg.citylist.mappers.CitiesDtoMapper;
import com.solbeg.citylist.model.City;
import com.solbeg.citylist.model.request.CityUpdateRequest;
import com.solbeg.citylist.repository.CitiesRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@AllArgsConstructor
public class CitiesServiceImpl implements CitiesService {

    private final CitiesRepository citiesRepository;
    private final CitiesDtoMapper dtoMapper = Mappers.getMapper(CitiesDtoMapper.class);

    @Override
    public Page<City> findPaginated(String searchText, Integer page, Integer size) {
        Sort defaultSort = Sort.by(Sort.Direction.ASC, "name");
        var dtoPage = citiesRepository.findByNameLikeIgnoreCase("%"+searchText+"%", PageRequest.of(page, size, defaultSort));
        return dtoPage.map(dtoMapper::dtoToDomain);
    }

    @Override
    public boolean updateCity(Long id, CityUpdateRequest updateRequest) {
        var cityDTO = citiesRepository.getReferenceById(id);
        if (StringUtils.hasText(updateRequest.getName()))
            cityDTO.setName(updateRequest.getName());
        if (StringUtils.hasText(updateRequest.getPhoto()))
            cityDTO.setPhoto(updateRequest.getPhoto());
        citiesRepository.save(cityDTO);
        return true;
    }
}
