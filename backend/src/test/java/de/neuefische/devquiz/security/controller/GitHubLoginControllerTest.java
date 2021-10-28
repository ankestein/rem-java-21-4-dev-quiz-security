package de.neuefische.devquiz.security.controller;


import de.neuefische.devquiz.security.model.GitHubAccessTokenDto;
import de.neuefische.devquiz.security.model.GitHubLoginDto;
import de.neuefische.devquiz.security.model.GitHubOAuthCredentialsDto;
import de.neuefische.devquiz.security.model.GitHubUserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubLoginControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private RestTemplate restTemplate;

    @Value("${de.neuefische.devquiz.github.clientId}")
    private String clientId;

    @Value("${de.neuefische.devquiz.github.secret}")
    private String clientSecret;

    @Value("${de.neuefische.devquiz.jwt.secret}")
    private String JWT_SECRET;

    @Test
    public void login() {
        // GIVEN
        GitHubLoginDto gitHubLoginDto = new GitHubLoginDto("some-code");

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setBearerAuth("some-access-token");

        when(restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                new HttpEntity<>(loginHeaders),
                GitHubUserDto.class)
        ).thenReturn(ResponseEntity.ok(new GitHubUserDto("some-login")));

        GitHubOAuthCredentialsDto credentialsDto = GitHubOAuthCredentialsDto.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(gitHubLoginDto.getCode())
                .build();

        HttpHeaders accessTokenHeader = new HttpHeaders();
        accessTokenHeader.setAccept(List.of(MediaType.APPLICATION_JSON));

        GitHubAccessTokenDto responseDto = new GitHubAccessTokenDto("some-access-token");

        when(restTemplate.exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                new HttpEntity<>(credentialsDto, accessTokenHeader),
                GitHubAccessTokenDto.class)
        ).thenReturn(ResponseEntity.ok(responseDto));

        // WHEN
        ResponseEntity<String> response = testRestTemplate.postForEntity("/auth/github/login", gitHubLoginDto, String.class);


        // THEN
        assertThat(response.getStatusCode(), Matchers.is(HttpStatus.OK));

        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(response.getBody()).getBody();
        assertThat(claims.getSubject(), Matchers.is("some-login"));

        verify(restTemplate).exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                new HttpEntity<>(credentialsDto, accessTokenHeader),
                GitHubAccessTokenDto.class);

        verify(restTemplate).exchange(  "https://api.github.com/user",
                HttpMethod.GET,
                new HttpEntity<>(loginHeaders),
                GitHubUserDto.class);

    }



}
