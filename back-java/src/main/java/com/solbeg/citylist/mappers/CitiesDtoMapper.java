package com.solbeg.citylist.mappers;

import com.solbeg.citylist.dto.CityDTO;
import com.solbeg.citylist.model.City;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper
public interface CitiesDtoMapper {
    City dtoToDomain(CityDTO source);
}
