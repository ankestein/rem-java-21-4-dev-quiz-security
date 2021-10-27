package de.neuefische.devquiz.security.service;

import de.neuefische.devquiz.security.api.GitHubApiService;
import de.neuefische.devquiz.security.model.GitHubUserDto;
import org.springframework.stereotype.Service;



@Service
public class GitHubLoginService {

    private final GitHubApiService gitHubApiService;

    public GitHubLoginService(GitHubApiService gitHubApiService) {
        this.gitHubApiService = gitHubApiService;
    }

    public String verifyGitHubLogin(String code) {

        // Verify code via GitHub
        String gitHubAccessToken =  gitHubApiService.getGitHubAccessToken(code);

        // Retrieve User Information
        GitHubUserDto gitHubUserDto = gitHubApiService.getUserInfo(gitHubAccessToken);

        // Create JWT access token
        return null;
    }





}

