package com.solbeg.citylist.controller;

import com.solbeg.citylist.model.City;
import com.solbeg.citylist.model.request.CityUpdateRequest;
import com.solbeg.citylist.model.response.CitiesListResponse;
import com.solbeg.citylist.model.response.CityUpdateResponse;
import com.solbeg.citylist.service.cities.CitiesService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/api/cities")
@AllArgsConstructor
public class CitiesController {
    private static final String FORBIDDEN_MSG = "User can't edit cities";

    private final CitiesService citiesService;

    @GetMapping
    public CitiesListResponse getCities(@RequestParam(defaultValue = "") String searchText, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        Page<City> resultPage = citiesService.findPaginated(searchText, page, size);

        return CitiesListResponse.builder()
                .cities(resultPage.getContent())
                .totalCount(resultPage.getTotalElements())
                .totalPages(resultPage.getTotalPages())
                .build();
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<CityUpdateResponse> updateCity(HttpServletRequest request, @PathVariable Long id, @RequestBody @Valid CityUpdateRequest updateRequest) {
        if (request.isUserInRole("ROLE_ALLOW_EDIT")) {
            return ResponseEntity.ok(citiesService.updateCity(id, updateRequest));
        }
        return ResponseEntity.status(FORBIDDEN).body(
                CityUpdateResponse.builder()
                        .isSuccess(false)
                        .message(FORBIDDEN_MSG)
                        .build()
        );
    }
}
