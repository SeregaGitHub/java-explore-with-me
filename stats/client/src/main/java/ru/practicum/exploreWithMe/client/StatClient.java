package ru.practicum.exploreWithMe.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.exploreWithMe.dto.HitDto;
import ru.practicum.exploreWithMe.dto.StatsDto;

import java.util.List;

@Service
public class StatClient {
    private static final String URL = "http://stats-server:9090";
    //private static final String URL = "http://localhost:9090";

    WebClient webClient = WebClient.builder()
            .baseUrl(URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public void addHit(HitDto hitDto) {
        Mono<String> postResponse = webClient
                .post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(hitDto)
                .retrieve()
                .bodyToMono(String.class);
        postResponse.block();
    }

    public List<StatsDto> getStatistic(String start, String end, List<String> uris, boolean unique) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris.toArray())
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToFlux(StatsDto.class)
                .collectList()
                .block();
    }
}

