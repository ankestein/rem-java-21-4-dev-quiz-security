package de.neuefische.devquiz.security.service;

import de.neuefische.devquiz.security.api.GitHubApiService;
import de.neuefische.devquiz.security.model.GitHubUserDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class GitHubLoginServiceTest {

    private final GitHubApiService gitHubApiService = mock(GitHubApiService.class);
    private final JWTUtilService jwtUtilService = mock(JWTUtilService.class);

    private final GitHubLoginService gitHubLoginService = new GitHubLoginService(gitHubApiService, jwtUtilService);

    @Test
    public void verifyGitHubLoginTest() {
        // GIVEN
        String code = "some-code";
        when(gitHubApiService.getGitHubAccessToken(code)).thenReturn("some-access-token");
        when(gitHubApiService.getUserInfo("some-access-token")).thenReturn(new GitHubUserDto("some-login"));
        when(jwtUtilService.createToken(new HashMap<>(), "some-login")).thenReturn("some-jwt");

        // WHEN
        String jwt = gitHubLoginService.verifyGitHubLogin(code);

        // THEN
        assertThat(jwt, Matchers.is("some-jwt"));

        verify(gitHubApiService).getGitHubAccessToken(code);
        verify(gitHubApiService).getUserInfo("some-access-token");
        verify(jwtUtilService).createToken(new HashMap<>(), "some-login");
    }

}
