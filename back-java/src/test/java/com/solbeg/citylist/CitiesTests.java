package com.solbeg.citylist;

import com.solbeg.citylist.dto.CityDTO;
import com.solbeg.citylist.model.request.CityUpdateRequest;
import com.solbeg.citylist.model.request.LoginRequest;
import com.solbeg.citylist.model.request.RegisterRequest;
import com.solbeg.citylist.model.response.CitiesListResponse;
import com.solbeg.citylist.model.response.CityUpdateResponse;
import com.solbeg.citylist.model.response.LoginResponse;
import com.solbeg.citylist.model.response.RegisterResponse;
import com.solbeg.citylist.repository.CitiesRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CitiesTests {

    private static final String BASE_API_URL = "http://localhost:8181/api";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CitiesRepository citiesRepository;

    @BeforeAll
    void setup() {
        createCity("city1", "photo1");
        createCity("city2", "photo2");

        var responseRegOkViewer = restTemplate.
                postForEntity(BASE_API_URL + "/register",
                        new RegisterRequest("testUserViewer", "testUserViewer", false),
                        RegisterResponse.class
                );
        assertEquals(responseRegOkViewer.getStatusCode(), HttpStatus.OK);

        var responseRegOkEditor = restTemplate.
                postForEntity(BASE_API_URL + "/register",
                        new RegisterRequest("testUserEditor", "testUserEditor", true),
                        RegisterResponse.class
                );
        assertEquals(responseRegOkEditor.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getCitiesList() {

        var token = loginAsViewer();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<CitiesListResponse> response = restTemplate.exchange(
                BASE_API_URL + "/cities",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CitiesListResponse.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        var cities = response.getBody().getCities();
        assertEquals(2, cities.size());
        assertTrue(cities.stream().anyMatch(c -> "city1".equals(c.getName())));
        assertTrue(cities.stream().anyMatch(c -> "city2".equals(c.getName())));
    }

    @Test
    void getFilteredCitiesList() {

        var token = loginAsViewer();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<CitiesListResponse> response = restTemplate.exchange(
                BASE_API_URL + "/cities?searchText=city1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CitiesListResponse.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        var cities = response.getBody().getCities();
        assertEquals(1, cities.size());
        assertTrue(cities.stream().anyMatch(c -> "city1".equals(c.getName())));
    }

    @Test
    void getPaginatedCitiesList() {

        var token = loginAsViewer();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<CitiesListResponse> response = restTemplate.exchange(
                BASE_API_URL + "/cities?page=0&size=1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CitiesListResponse.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        var cities = response.getBody().getCities();
        assertEquals(1, cities.size());
        assertTrue(cities.stream().anyMatch(c -> "city1".equals(c.getName())));

        ResponseEntity<CitiesListResponse> response1 = restTemplate.exchange(
                BASE_API_URL + "/cities?page=1&size=1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CitiesListResponse.class
        );

        assertEquals(response1.getStatusCode(), HttpStatus.OK);
        assertNotNull(response1.getBody());
        var cities1 = response1.getBody().getCities();
        assertEquals(1, cities1.size());
        assertTrue(cities1.stream().anyMatch(c -> "city2".equals(c.getName())));
    }

    @Test
    void searchCityByName() {
        createCity("cityForSearch", "photoForSearch");
        var token = loginAsEditor();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<CitiesListResponse> responseList = restTemplate.exchange(
                BASE_API_URL + "/cities?searchText=cityForSearch",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CitiesListResponse.class
        );

        assertEquals(responseList.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseList.getBody());
        var cities = responseList.getBody().getCities();
        assertEquals(1, cities.size());
    }

    @Test
    void editCity() {

        var cityForEdit = createCity("cityForEdit", "photoForEdit");
        var token = loginAsEditor();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        var editRequest = new CityUpdateRequest("cityForEditNewName", "cityForEditNewPhoto");
        var responseUpdate = restTemplate.exchange(
                BASE_API_URL + "/cities/" + cityForEdit.getId(),
                HttpMethod.POST,
                new HttpEntity<>(editRequest, headers),
                CityUpdateResponse.class
        );
        assertEquals(responseUpdate.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseUpdate.getBody());
        assertTrue(responseUpdate.getBody().isSuccess());

        ResponseEntity<CitiesListResponse> responseListWithUpdated = restTemplate.exchange(
                BASE_API_URL + "/cities?searchText=cityForEditNewName",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CitiesListResponse.class
        );
        assertEquals(responseListWithUpdated.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseListWithUpdated.getBody());
        var citiesWithUpdated = responseListWithUpdated.getBody().getCities();
        assertEquals(1, citiesWithUpdated.size());
        assertEquals(cityForEdit.getId(), citiesWithUpdated.get(0).getId());
        assertEquals(editRequest.getName(), citiesWithUpdated.get(0).getName());
        assertEquals(editRequest.getPhoto(), citiesWithUpdated.get(0).getPhoto());
    }

    private String loginAsViewer() {
        var responseLoginOk = restTemplate.
                postForEntity(BASE_API_URL + "/login",
                        new LoginRequest("testUserViewer", "testUserViewer"),
                        LoginResponse.class
                );
        assertEquals(responseLoginOk.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseLoginOk.getBody());
        assertNotNull(responseLoginOk.getBody().getJwtToken());
        return responseLoginOk.getBody().getJwtToken();
    }

    private String loginAsEditor() {
        var responseLoginOk = restTemplate.
                postForEntity(BASE_API_URL + "/login",
                        new LoginRequest("testUserEditor","testUserEditor"),
                        LoginResponse.class
                );
        assertEquals(responseLoginOk.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseLoginOk.getBody());
        assertNotNull(responseLoginOk.getBody().getJwtToken());
        return responseLoginOk.getBody().getJwtToken();
    }

    private CityDTO createCity(String name, String photo) {
        var cityDto = new CityDTO();
        cityDto.setName(name);
        cityDto.setPhoto(photo);

        var createdCity = citiesRepository.save(cityDto);
        assertEquals(cityDto.getName(), createdCity.getName());
        assertEquals(cityDto.getPhoto(), createdCity.getPhoto());
        return createdCity;
    }
}
