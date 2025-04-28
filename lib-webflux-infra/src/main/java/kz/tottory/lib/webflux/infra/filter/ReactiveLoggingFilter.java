package kz.tottory.lib.webflux.infra.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ReactiveLoggingFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        logRequest(request);

        return chain.filter(new CastServerWebExchangeDecorator(exchange));
    }

    private void logRequest(ServerHttpRequest request) {
        String headers = request.getHeaders().entrySet().stream()
                .map(e -> e.getKey() + ": " + String.join(",", e.getValue()))
                .reduce((h1, h2) -> h1 + ", " + h2)
                .orElse("");

        log.info("[REQUEST] {} {} Headers=[{}]", request.getMethod(), request.getURI(), headers);
    }

    private void logResponse(String responseBody, HttpStatusCode status) {
        log.info("[RESPONSE] Status={} Body={}", status.value(), responseBody);
    }
}
