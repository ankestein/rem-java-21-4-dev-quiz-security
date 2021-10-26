package de.neuefische.devquiz.security.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GitHubOAuthCredentialsDto {

    String clientId;
    String clientSecret;
    String code;

}
