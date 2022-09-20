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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.http.HttpResponse;

import static com.solbeg.citylist.model.UserRole.ROLE_ALLOW_EDIT;
import static com.solbeg.citylist.model.UserRole.ROLE_BASIC;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SecurityLoginTests {

    private static final String BASE_API_URL = "http://localhost:8181/api";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CitiesRepository citiesRepository;

    @Test
    void correctRegisterAndLoginAsViewer() {
        registerUser(new RegisterRequest("view", "view", false));

        var responseLogin = restTemplate.
                postForEntity(BASE_API_URL + "/login",
                        new LoginRequest("view", "view"),
                        LoginResponse.class
                );

        assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseLogin.getBody());
        assertNotNull(responseLogin.getBody().getJwtToken());
        assertTrue(responseLogin.getBody().getRoles().stream().anyMatch(r -> r.equals(ROLE_BASIC.name())));
    }

    @Test
    void correctRegisterAndLoginAsEditor() {
        registerUser(new RegisterRequest("editor", "editor", true));

        var responseLogin = restTemplate.
                postForEntity(BASE_API_URL + "/login",
                        new LoginRequest("editor", "editor"),
                        LoginResponse.class
                );

        assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseLogin.getBody());
        assertNotNull(responseLogin.getBody().getJwtToken());
        assertTrue(responseLogin.getBody().getRoles().stream().anyMatch(r -> r.equals(ROLE_ALLOW_EDIT.name())));
    }

    @Test
    void registerWithSameName() {
        var registerRequest = new RegisterRequest("viewSameName", "viewSameName", false);
        registerUser(registerRequest);

        var response = restTemplate.
                postForEntity(BASE_API_URL + "/register", registerRequest, RegisterResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getMessage(), "User has already in db");
    }

    @Test
    void failLoginNotRegister() {
        var responseLogin = restTemplate.
                postForEntity(BASE_API_URL + "/login", new LoginRequest("nobody", "nobody"), LoginResponse.class);
        assertEquals(responseLogin.getStatusCode(), HttpStatus.FORBIDDEN);
        assertNotNull(responseLogin.getBody());
        assertFalse(responseLogin.getBody().isSuccess());
    }

    @Test
    void noLoginUsersCantView() {
        var response = restTemplate.
                getForEntity(BASE_API_URL + "/cities", CitiesListResponse.class);
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    void noLoginUsersCantEdit() {
        var createdCity = createCity("CityNoLoginUsersCantEdit","CityNoLoginUsersCantEdit");
        var response = restTemplate.
                postForEntity(BASE_API_URL + "/cities/" + createdCity.getId(),
                        new CityUpdateRequest("newName", "newPhoto"),
                        Boolean.class);

        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    void viewerCantEdit() {
        registerUser(new RegisterRequest("viewerCantEdit", "viewerCantEdit", false));
        var createdCity = createCity("CityViewerCantCantEdit","CityViewerCantCantEdit");
        var responseLoginOk = restTemplate.
                postForEntity(BASE_API_URL + "/login",
                        new LoginRequest("viewerCantEdit", "viewerCantEdit"),
                        LoginResponse.class
                );
        assertEquals(responseLoginOk.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseLoginOk.getBody());
        assertNotNull(responseLoginOk.getBody().getJwtToken());

        var headers = new HttpHeaders();
        headers.setBearerAuth(responseLoginOk.getBody().getJwtToken());

        var editRequest = new CityUpdateRequest("newName", "newPhoto");
        var responseUpdate = restTemplate.exchange(
                BASE_API_URL + "/cities/" + createdCity.getId(),
                HttpMethod.POST,
                new HttpEntity<>(editRequest,headers),
                CityUpdateResponse.class
        );
        assertEquals(responseUpdate.getStatusCode(), HttpStatus.FORBIDDEN);
        assertNotNull(responseUpdate.getBody());
        assertFalse(responseUpdate.getBody().isSuccess());
    }

    @Test
    void fakeTokenCantEdit() {
        var createdCity = createCity("nameFakeTokenCantEdit", "photoFakeTokenCantEdit");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("fakeToken");

        var updateRequest = new CityUpdateRequest("newName", "newPhoto");
        var responseUpdate = restTemplate.exchange(
                BASE_API_URL + "/cities/" + createdCity.getId(),
                HttpMethod.POST,
                new HttpEntity<>(updateRequest, headers),
                Boolean.class
        );

        assertEquals(responseUpdate.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    private void registerUser(RegisterRequest registerRequest) {
        var response = restTemplate.
                postForEntity(BASE_API_URL + "/register", registerRequest, RegisterResponse.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getMessage(), "User was saved");
    }

    private CityDTO createCity(String name, String photo){
        var cityDto = new CityDTO();
        cityDto.setName(name);
        cityDto.setPhoto(photo);

        var createdCity = citiesRepository.save(cityDto);
        assertEquals(cityDto.getName(), createdCity.getName());
        assertEquals(cityDto.getPhoto(), createdCity.getPhoto());
        return createdCity;
    }

}
