package de.neuefische.devquiz.security.controller;

import de.neuefische.devquiz.security.model.GitHubLoginDto;
import de.neuefische.devquiz.security.service.GitHubLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth/github/login")
public class GitHubLoginController {

    private final GitHubLoginService gitHubLoginService;

    @Autowired
    public GitHubLoginController(GitHubLoginService gitHubLoginService) {
        this.gitHubLoginService = gitHubLoginService;
    }

    @PostMapping
    public String login(@RequestBody GitHubLoginDto gitHubLoginDto) {

        // Verify code via GitHub by getting a GitHub AccessToken
        String gitHubAccessToken = gitHubLoginService.verifyGitHubLogin(gitHubLoginDto.getCode());

        log.error("THIS IS THE ACCESS TOKEN: " + gitHubAccessToken);

        return gitHubAccessToken;
        // service for post request
        // return GitHubAccessToken;
    }


}
