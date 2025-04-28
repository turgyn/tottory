package kz.tottory.app.balance.impl;

import kz.tottory.lib.user.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UserClient {

    private final WebClient webClient;

    public UserClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080")
                .build();
    }

    public Mono<UserDto> getUser(Long userId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/users/1").build())
                .retrieve()
                .bodyToMono(UserDto.class)
                .doOnSuccess(rs -> log.info("Got response from user: {}", rs))
                .doOnError(WebClientResponseException.class, e -> {
                    log.error("Got error from user: {} {}", e.getMessage(), e.getResponseBodyAsString(), e);
                })
                .doOnError(e -> !(e instanceof WebClientResponseException), e -> {
                    log.error("Got error from user: {}", e.getMessage(), e);
                });
    }
}
