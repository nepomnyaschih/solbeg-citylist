package com.solbeg.citylist.repository;

import com.solbeg.citylist.dto.CityDTO;;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitiesRepository  extends JpaRepository<CityDTO, Long> {
    Page<CityDTO> findByNameLikeIgnoreCase(String name, Pageable pageable);
}

