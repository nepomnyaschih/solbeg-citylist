package com.solbeg.citylist.controller;

import com.solbeg.citylist.model.City;
import com.solbeg.citylist.model.request.CityUpdateRequest;
import com.solbeg.citylist.model.response.CitiesListResponse;
import com.solbeg.citylist.service.cities.CitiesService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

    @PostMapping(path = "/{id}")
    public boolean updateCity(@PathVariable Long id, HttpServletRequest request, @RequestBody CityUpdateRequest updateRequest){
        if (request.isUserInRole("ROLE_ALLOW_EDIT")) {
            return citiesService.updateCity(id, updateRequest);
        } else {
            //todo exception for access denied
            return false;
        }
    }

}
