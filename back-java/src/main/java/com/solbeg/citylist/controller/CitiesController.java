package com.solbeg.citylist.controller;

import com.solbeg.citylist.model.City;
import com.solbeg.citylist.model.response.CitiesListResponse;
import com.solbeg.citylist.service.cities.CitiesService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cities")
@AllArgsConstructor
public class CitiesController {
    private final CitiesService citiesService;

    @GetMapping
    public CitiesListResponse getCities(    @RequestParam(defaultValue = "") String searchText,
                                            @RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Page<City> resultPage = citiesService.findPaginated(searchText, page, size);
        return new CitiesListResponse(resultPage.getContent(), resultPage.getTotalElements(), resultPage.getTotalPages());
    }

}
