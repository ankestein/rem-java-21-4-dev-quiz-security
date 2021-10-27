package de.neuefische.devquiz.security.service;

import org.springframework.stereotype.Service;



@Service
public class GitHubLoginService {

    private final GitHubApiService gitHubApiService;

    public GitHubLoginService(GitHubApiService gitHubApiService) {
        this.gitHubApiService = gitHubApiService;
    }

    public String verifyGitHubLogin(String code) {

        String gitHubAccessToken =  gitHubApiService.getGitHubAccessToken(code);
        //System.out.println("THIS IS THE ACCESS TOKEN: " + gitHubAccessToken);
        return gitHubAccessToken;


    }





}

