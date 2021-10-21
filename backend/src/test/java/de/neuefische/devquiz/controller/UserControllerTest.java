package de.neuefische.devquiz.controller;

import de.neuefische.devquiz.security.model.AppUser;
import de.neuefische.devquiz.security.model.UserResponseDto;
import de.neuefische.devquiz.security.repo.AppUserRepo;
import de.neuefische.devquiz.security.service.JWTUtilService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private AppUserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JWTUtilService jwtUtilService;


    @Test
    public void getLoggedInUserWithValidTokenTest() {
        // GIVEN
        HttpHeaders headers = getHttpHeadersWithJWT();

        // WHEN
        ResponseEntity<UserResponseDto> response = testRestTemplate.exchange("/api/user/me", HttpMethod.GET, new HttpEntity<>(headers), UserResponseDto.class);

        // THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat("test-username", is(response.getBody().getUsername()));
     }

    @Test
    public void getLoggedInUserWithInvalidTokenTest() {
        // GIVEN
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("ey.thisisnotavalidtoken.notatall");

        // WHEN
        ResponseEntity<UserResponseDto> response = testRestTemplate.exchange("/api/user/me", HttpMethod.GET, new HttpEntity<>(headers), UserResponseDto.class);

        // THEN
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }


    @Test
    public void getLoggedInUserWithExpiredTokenTest() {
        // GIVEN
        HttpHeaders headers = new HttpHeaders();

        ReflectionTestUtils.setField(jwtUtilService, "duration", 1);
        headers.setBearerAuth(jwtUtilService.createToken(new HashMap<>(), "test-username"));

        // WHEN
        ResponseEntity<UserResponseDto> response = testRestTemplate.exchange("/api/user/me", HttpMethod.GET, new HttpEntity<>(headers), UserResponseDto.class);

        // THEN
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }





    private HttpHeaders getHttpHeadersWithJWT() {
        userRepo.save(AppUser.builder()
                .username("test-username")
                .password(passwordEncoder.encode("test-password"))
                .build());
        AppUser loginData = new AppUser("test-username", "test-password");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/auth/login", loginData, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(response.getBody());
        return(headers);
    }

}
