package de.neuefische.devquiz.security.api;

import de.neuefische.devquiz.controller.exception.GitHubAuthException;
import de.neuefische.devquiz.security.model.GitHubAccessTokenDto;
import de.neuefische.devquiz.security.model.GitHubOAuthCredentialsDto;
import de.neuefische.devquiz.security.model.GitHubUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class GitHubApiService {

    private final RestTemplate restTemplate;

    private static final String GITHUB_CODE_URL = "https://github.com/login/oauth/access_token";
    private static final String GITHUB_USER_URL = "https://api.github.com/user";

    @Value("${de.neuefische.devquiz.github.clientId}")
    private String clientId;

    @Value("${de.neuefische.devquiz.github.secret}")
    private String clientSecret;

    public GitHubApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getGitHubAccessToken(String code) {
        GitHubOAuthCredentialsDto credentialsDto = GitHubOAuthCredentialsDto.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(code)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<GitHubAccessTokenDto> responseEntity = restTemplate.exchange(
                GITHUB_CODE_URL,
                HttpMethod.POST,
                new HttpEntity<>(credentialsDto, httpHeaders),
                GitHubAccessTokenDto.class);

        if (responseEntity.getBody() == null) {
            throw new GitHubAuthException("Error while authenticating with code via GitHub! Body is null!");
        }

        log.info("HERE IS THE RESPONSE: " + responseEntity);
        log.error("HERE IS THE RESPONSE: " + responseEntity);
        return (responseEntity.getBody().getGitHubAccessToken());

    }

    public GitHubUserDto getUserInfo(String gitHubAccessToken) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(gitHubAccessToken);

        ResponseEntity<GitHubUserDto> responseEntity = restTemplate.exchange(
                GITHUB_USER_URL,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                GitHubUserDto.class);

        if (responseEntity.getBody() == null) {
            throw new GitHubAuthException("Error while authenticating with code via GitHub! Body is null!");
        }

        return responseEntity.getBody();

    }
}
