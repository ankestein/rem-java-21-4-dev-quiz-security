package de.neuefische.devquiz.security.service;

import de.neuefische.devquiz.security.model.GitHubOAuthCredentialsDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubApiService {

    private String clientId = "20bd6360403e3e1711a8";
    private String clientSecret = "88055ae99d4e167fc3d648d0038216e5ad7c0bd6";
    private RestTemplate restTemplate;

    public String getGitHubAccessToken(String code) {
        GitHubOAuthCredentialsDto credentialsDto = GitHubOAuthCredentialsDto.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(code)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();

        ResponseEntity<String> responseEntity = restTemplate.exchange("https://github.com/login/oauth/access_token",
                HttpMethod.POST, new HttpEntity<>(credentialsDto, httpHeaders), String.class );

        //System.out.println("HERE IS THE RESPONSE: " + responseEntity);
        return(responseEntity.getBody());

    }
}
