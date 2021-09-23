package com.example.apiclient;

import com.example.domain.github.GitHubPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.example.util.LoggerUtil.log;
import static org.junit.jupiter.api.Assertions.*;

/** Class deprecated
 * @deprecated
 * GitHub no longer supports GitHub Jobs API
 * */
@Deprecated
@Disabled
class GitHubJobsClientTest {

    WebClient webClient = WebClient.create("https://jobs.github.com");
    GitHubJobsClient jobsClient = new GitHubJobsClient(webClient);

    @Test
    void invokeGithubJobsAPIWithPageNumber() {
        //given
        int pageNum = 1;
        String description = "Java";

        //when
        List<GitHubPosition> gitHubPositions = jobsClient.invokeGithubJobsAPIWithPageNumber(pageNum, description);
        log("Github positions: " + gitHubPositions);

        //then
        assertTrue(gitHubPositions.size() > 0);
        gitHubPositions.forEach(Assertions::assertNotNull);
    }

    @Test
    void invokeGithubJobsApiWithMultiplePageNumbersCF() {

        //given
        String jobDescription = "Java";
        List<Integer> pageNumbers= List.of(1,2,3);
        //when
        List<GitHubPosition> gitHubPositionList  =
                jobsClient.invokeGithubJobsApiWithMultiplePageNumbersCF(pageNumbers, jobDescription);

        //then
        assertTrue(gitHubPositionList.size() > 0);
    }

    @Test
    void invokeGithubJobsApiWithMultiplePageNumberList() {

        //given
        String jobDescription = "Java";
        List<Integer> pageNumbers= List.of(1,2,3);
        //when
        List<GitHubPosition> gitHubPositionList  =
                jobsClient.invokeGithubJobsApiWithPageNumberList(pageNumbers, jobDescription);

        //then
        assertTrue(gitHubPositionList.size() > 0);
    }
}