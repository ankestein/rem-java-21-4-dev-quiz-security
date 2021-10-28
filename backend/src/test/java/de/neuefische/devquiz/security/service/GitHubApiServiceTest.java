package de.neuefische.devquiz.security.service;

import de.neuefische.devquiz.security.api.GitHubApiService;
import de.neuefische.devquiz.security.model.GitHubAccessTokenDto;
import de.neuefische.devquiz.security.model.GitHubOAuthCredentialsDto;
import de.neuefische.devquiz.security.model.GitHubUserDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class GitHubApiServiceTest {

    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final GitHubApiService gitHubApiService = new GitHubApiService(restTemplate);

    @Value("${de.neuefische.devquiz.github.clientId}")
    private String clientId;

    @Value("${de.neuefische.devquiz.github.clientSecret}")
    private String clientSecret;


    @Test
    public void getGitHubAccessTokenTest() {
        // GIVEN
        String code = "some-code";

        GitHubOAuthCredentialsDto credentialsDto = GitHubOAuthCredentialsDto.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(code)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        GitHubAccessTokenDto responseDto = new GitHubAccessTokenDto("some-access-token");

        when(restTemplate.exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                new HttpEntity<>(credentialsDto, httpHeaders),
                GitHubAccessTokenDto.class)
        ).thenReturn(ResponseEntity.ok(responseDto));

        // WHEN
        String token = gitHubApiService.getGitHubAccessToken(code);

        // THEN
        assertThat(token, Matchers.is("some-access-token"));
        verify(restTemplate).exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                new HttpEntity<>(credentialsDto, httpHeaders),
                GitHubAccessTokenDto.class);
    }


    @Test
    public void getUserInfoTest(){
        // GIVEN
        String token = "some-access-token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);

        when(restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                GitHubUserDto.class)
        ).thenReturn(ResponseEntity.ok(new GitHubUserDto("some-login")));

        // WHEN
        GitHubUserDto gitHubUserDto = gitHubApiService.getUserInfo(token);

        // THEN
        assertThat(gitHubUserDto.getLogin(), Matchers.is("some-login"));
        verify(restTemplate).exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                GitHubUserDto.class);
    }
}
