package com.example.apiclient;

import com.example.domain.github.GitHubPosition;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.example.util.CommonUtil.startTimer;
import static com.example.util.CommonUtil.timeTaken;
import static com.example.util.LoggerUtil.log;

/** Class deprecated
 * @deprecated
 * GitHub no longer supports GitHub Jobs API
 * */
@Deprecated
public class GitHubJobsClient {

    private WebClient webClient;

    public GitHubJobsClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<GitHubPosition> invokeGithubJobsAPIWithPageNumber(int pageNumber, String description){

        String uri = UriComponentsBuilder.fromUriString("/positions.json")
                .queryParam("description", description)
                .queryParam("page", pageNumber)
                .buildAndExpand()
                .toUriString();
        log("uri: " + uri);

        return webClient.get().uri(uri)
                .retrieve()
                .bodyToFlux(GitHubPosition.class)
                .collectList()
                .block();
    }

    public List<GitHubPosition> invokeGithubJobsApiWithMultiplePageNumbersCF(List<Integer> pageList, String description) {
        startTimer();

        List<CompletableFuture<List<GitHubPosition>>> gitHubPositions = pageList.stream()
                .map(pageNum -> CompletableFuture.supplyAsync(() -> invokeGithubJobsAPIWithPageNumber(pageNum, description)))
                .collect(Collectors.toList());

        CompletableFuture<Void> cfAllOf = CompletableFuture.allOf(gitHubPositions.toArray(new CompletableFuture[0]));

        List<GitHubPosition>  gitHubPositionsList =  cfAllOf
                .thenApply(v-> gitHubPositions.stream().map(CompletableFuture::join)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()))
                .join();

        timeTaken();
        return gitHubPositionsList;
    }

    public List<GitHubPosition> invokeGithubJobsApiWithPageNumberList(List<Integer> pageList, String description) {
        startTimer();

        List<GitHubPosition> gitHubPositionsList = pageList.stream()
                .map(pageNum ->  invokeGithubJobsAPIWithPageNumber(pageNum, description))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        timeTaken();
        return gitHubPositionsList;
    }


}
