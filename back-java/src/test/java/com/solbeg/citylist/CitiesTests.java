package com.solbeg.citylist;

import com.solbeg.citylist.dto.CityDTO;
import com.solbeg.citylist.model.request.CityUpdateRequest;
import com.solbeg.citylist.model.request.LoginRequest;
import com.solbeg.citylist.model.request.RegisterRequest;
import com.solbeg.citylist.model.response.CitiesListResponse;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CitiesTests {

    private static final String BASE_API_URL = "http://localhost:8080/api";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CitiesRepository citiesRepository;

    @BeforeAll
    void setup() {
        var cityDto1 = new CityDTO();
        cityDto1.setName("city1");
        cityDto1.setPhoto("photo1");

        var createdCity1 = citiesRepository.save(cityDto1);
        assertEquals(cityDto1.getName(), createdCity1.getName());
        assertEquals(cityDto1.getPhoto(), createdCity1.getPhoto());

        var cityDto2 = new CityDTO();
        cityDto2.setName("city2");
        cityDto2.setPhoto("photo2");

        var createdCity2 = citiesRepository.save(cityDto2);
        assertEquals(cityDto2.getName(), createdCity2.getName());
        assertEquals(cityDto2.getPhoto(), createdCity2.getPhoto());

        var registerRequestViewer = new RegisterRequest();
        registerRequestViewer.setUsername("testUserViewer");
        registerRequestViewer.setPassword("testUserViewer");
        registerRequestViewer.setCanEdit(false);

        var responseRegOkViewer = restTemplate.
                postForEntity(BASE_API_URL + "/register", registerRequestViewer, RegisterResponse.class);
        assertEquals(responseRegOkViewer.getStatusCode(), HttpStatus.OK);

        var registerRequestEditor = new RegisterRequest();
        registerRequestEditor.setUsername("testUserEditor");
        registerRequestEditor.setPassword("testUserEditor");
        registerRequestEditor.setCanEdit(true);

        var responseRegOkEditor = restTemplate.
                postForEntity(BASE_API_URL + "/register", registerRequestEditor, RegisterResponse.class);
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
        assertTrue(cities.stream().anyMatch(c->"city1".equals(c.getName())));
        assertTrue(cities.stream().anyMatch(c->"city2".equals(c.getName())));
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
        assertTrue(cities.stream().anyMatch(c->"city1".equals(c.getName())));
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
        assertTrue(cities.stream().anyMatch(c->"city1".equals(c.getName())));

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
        assertTrue(cities1.stream().anyMatch(c->"city2".equals(c.getName())));
    }

    @Test
    void editCity() {

        var cityDto = new CityDTO();
        cityDto.setName("cityForEdit");
        cityDto.setPhoto("photoForEdit");

        var createdCity1 = citiesRepository.save(cityDto);
        assertEquals(cityDto.getName(), createdCity1.getName());
        assertEquals(cityDto.getPhoto(), createdCity1.getPhoto());

        var token = loginAsEditor();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<CitiesListResponse> responseList = restTemplate.exchange(
                BASE_API_URL +"/cities?searchText=cityForEdit",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CitiesListResponse.class
        );

        assertEquals(responseList.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseList.getBody());
        var cities = responseList.getBody().getCities();
        assertEquals(1, cities.size());

        var editRequest = new CityUpdateRequest();
        editRequest.setName("cityForEditNewName");
        editRequest.setPhoto("cityForEditNewPhoto");

        var responseUpdate = restTemplate.exchange(
                BASE_API_URL + "/cities/" + cities.get(0).getId(),
                HttpMethod.POST,
                new HttpEntity<>(editRequest,headers),
                Boolean.class
        );
        assertEquals(responseUpdate.getStatusCode(), HttpStatus.OK);
        assertEquals(Boolean.TRUE, responseUpdate.getBody());

        ResponseEntity<CitiesListResponse> responseListWithUpdated = restTemplate.exchange(
                BASE_API_URL +"/cities?searchText=cityForEditNewName",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CitiesListResponse.class
        );
        assertEquals(responseListWithUpdated.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseListWithUpdated.getBody());
        var citiesWithUpdated = responseListWithUpdated.getBody().getCities();
        assertEquals(1, citiesWithUpdated.size());
        assertEquals(editRequest.getName(), citiesWithUpdated.get(0).getName());
        assertEquals(editRequest.getPhoto(), citiesWithUpdated.get(0).getPhoto());
    }

    private String loginAsViewer(){
        var loginRequest = new LoginRequest();
        loginRequest.setUsername("testUserViewer");
        loginRequest.setPassword("testUserViewer");

        var responseLoginOk = restTemplate.
                postForEntity(BASE_API_URL + "/login", loginRequest, LoginResponse.class);
        assertEquals(responseLoginOk.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseLoginOk.getBody());
        assertNotNull(responseLoginOk.getBody().getJwtToken());

        return responseLoginOk.getBody().getJwtToken();
    }

    private String loginAsEditor(){
        var loginRequest = new LoginRequest();
        loginRequest.setUsername("testUserEditor");
        loginRequest.setPassword("testUserEditor");

        var responseLoginOk = restTemplate.
                postForEntity(BASE_API_URL + "/login", loginRequest, LoginResponse.class);
        assertEquals(responseLoginOk.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseLoginOk.getBody());
        assertNotNull(responseLoginOk.getBody().getJwtToken());

        return responseLoginOk.getBody().getJwtToken();
    }
}
