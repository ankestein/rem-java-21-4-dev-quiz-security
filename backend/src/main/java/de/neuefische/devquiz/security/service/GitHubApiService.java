package de.neuefische.devquiz.security.service;

import de.neuefische.devquiz.controller.exception.GitHubAuthException;
import de.neuefische.devquiz.security.model.GitHubAccessTokenDto;
import de.neuefische.devquiz.security.model.GitHubOAuthCredentialsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class GitHubApiService {

    private final RestTemplate restTemplate;

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
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                new HttpEntity<>(credentialsDto, httpHeaders),
                GitHubAccessTokenDto.class);

        if(responseEntity.getBody() == null){
            throw new GitHubAuthException("Error while authenticating with code via GitHub! Body is null!");
        }

        System.out.println("HERE IS THE RESPONSE: " + responseEntity);
        return (responseEntity.getBody().getGitHubAccessToken());

    }
}
