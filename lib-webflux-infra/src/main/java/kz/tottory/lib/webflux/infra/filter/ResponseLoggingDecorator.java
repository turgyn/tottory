package kz.tottory.lib.webflux.infra.filter;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class ResponseLoggingDecorator extends ServerHttpResponseDecorator {


    public ResponseLoggingDecorator(ServerHttpResponse delegate) {
        super(delegate);
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        if (!Objects.requireNonNull(getHeaders().getContentType()).equalsTypeAndSubtype(MimeType.valueOf("application/json"))) {
            log.info("Response: statusCode={} payload=(not json)", getStatusCode() != null ? getStatusCode().value() : "UNKNOWN");
            return super.writeWith(body);
        }

        return super.writeWith(Flux.from(body)
                .mapNotNull(dataBuffer -> {
                    String responseBody = dataBuffer.toString(StandardCharsets.UTF_8);
                    log.info("Response: statusCode={} payload={}", getStatusCode() != null ? getStatusCode().value() : "UNKNOWN", responseBody);
                    return dataBuffer;
                })
                .switchIfEmpty(subscriber -> log.info("Response: body=(not json)")));
    }
}