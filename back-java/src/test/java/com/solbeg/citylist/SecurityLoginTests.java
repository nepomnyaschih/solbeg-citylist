package com.solbeg.citylist;

import com.solbeg.citylist.dto.CityDTO;
import com.solbeg.citylist.model.request.CityUpdateRequest;
import com.solbeg.citylist.model.request.LoginRequest;
import com.solbeg.citylist.model.request.RegisterRequest;
import com.solbeg.citylist.model.response.CitiesListResponse;
import com.solbeg.citylist.model.response.LoginResponse;
import com.solbeg.citylist.model.response.RegisterResponse;
import com.solbeg.citylist.repository.CitiesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SecurityLoginTests {

    private static final String BASE_API_URL = "http://localhost:8080/api";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CitiesRepository citiesRepository;

    @Test
    void correctRegisterAndLoginAsViewer() {

        var registerRequest = new RegisterRequest();
        registerRequest.setUsername("view");
        registerRequest.setPassword("view");
        registerRequest.setCanEdit(false);

        var responseRegOk = restTemplate.
                postForEntity(BASE_API_URL + "/register", registerRequest, RegisterResponse.class);

        assertEquals(responseRegOk.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseRegOk.getBody());
        assertEquals(responseRegOk.getBody().getMessage(), "User was saved");

       var responseErr = restTemplate.
                postForEntity(BASE_API_URL+  "/register", registerRequest, RegisterResponse.class);

        assertEquals(responseErr.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);

        var loginRequest = new LoginRequest();
        loginRequest.setUsername("view");
        loginRequest.setPassword("view");

       var responseLoginOk = restTemplate.
                postForEntity(BASE_API_URL + "/login", loginRequest, LoginResponse.class);

        assertEquals(responseLoginOk.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseLoginOk.getBody());
        assertNotNull(responseLoginOk.getBody().getJwtToken());
        assertTrue(responseLoginOk.getBody().getRoles().stream().anyMatch(r -> r.equals("ROLE_BASIC")));

    }

    @Test
    void correctRegisterAndLoginAsEditor() {

        var registerRequest = new RegisterRequest();
        registerRequest.setUsername("editor");
        registerRequest.setPassword("editor");
        registerRequest.setCanEdit(true);

        var responseRegOk = restTemplate.
                postForEntity(BASE_API_URL + "/register", registerRequest, RegisterResponse.class);

        assertEquals(responseRegOk.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseRegOk.getBody());
        assertEquals(responseRegOk.getBody().getMessage(), "User was saved");

        var loginRequest = new LoginRequest();
        loginRequest.setUsername("editor");
        loginRequest.setPassword("editor");

        var responseLoginOk = restTemplate.
                postForEntity(BASE_API_URL + "/login", loginRequest, LoginResponse.class);

        assertEquals(responseLoginOk.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseLoginOk.getBody());
        assertNotNull(responseLoginOk.getBody().getJwtToken());
        assertTrue(responseLoginOk.getBody().getRoles().stream().anyMatch(r -> r.equals("ROLE_ALLOW_EDIT")));

    }

    @Test
    void failLoginNotRegister() {
        var loginRequest = new LoginRequest();
        loginRequest.setUsername("nobody");
        loginRequest.setPassword("nobody");

        var responseLoginOk = restTemplate.
                postForEntity(BASE_API_URL+ "/login", loginRequest, LoginResponse.class);

        assertEquals(responseLoginOk.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    void noLoginUsersCantView() {
        var response = restTemplate.
                getForEntity(BASE_API_URL + "/cities", CitiesListResponse.class);
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    void noLoginUsersCantEdit() {
        var cityDto = new CityDTO();
        cityDto.setName("name");
        cityDto.setPhoto("photo");

        var createdCity = citiesRepository.save(cityDto);
        assertEquals(cityDto.getName(), createdCity.getName());
        assertEquals(cityDto.getPhoto(), createdCity.getPhoto());

        var updateRequest = new CityUpdateRequest();
        updateRequest.setName("newName");
        updateRequest.setPhoto("newPhoto");

       var response = restTemplate.
                postForEntity(BASE_API_URL + "/cities/" + createdCity.getId(), updateRequest, Boolean.class);
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    void fakeTokenCantEdit() {
        var cityDto = new CityDTO();
        cityDto.setName("nameFakeTokenCantEdit");
        cityDto.setPhoto("photoFakeTokenCantEdit");

        var createdCity = citiesRepository.save(cityDto);
        assertEquals(cityDto.getName(), createdCity.getName());
        assertEquals(cityDto.getPhoto(), createdCity.getPhoto());

        var updateRequest = new CityUpdateRequest();
        updateRequest.setName("newName");
        updateRequest.setPhoto("newPhoto");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("fakeToken");
        var responseUpdate = restTemplate.exchange(
                BASE_API_URL + "/cities/" + createdCity.getId(),
                HttpMethod.POST,
                new HttpEntity<>(updateRequest,headers),
                Boolean.class
        );

        assertEquals(responseUpdate.getStatusCode(), HttpStatus.FORBIDDEN);
    }

}
