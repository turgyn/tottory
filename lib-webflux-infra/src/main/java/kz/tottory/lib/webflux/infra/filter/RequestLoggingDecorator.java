package kz.tottory.lib.webflux.infra.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class RequestLoggingDecorator extends ServerHttpRequestDecorator {

    private final Set<String> headersToHide = Set.of("AUTHORIZATION", "COOKIE", "ANTIFRAUD");
    private final String headers;

    public RequestLoggingDecorator(ServerHttpRequest delegate) {
        super(delegate);
        this.headers = super.getHeaders().entrySet()
                .stream()
                .filter((key) -> !headersToHide.contains(key.getKey().toUpperCase()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                .toString();
    }

    @Override
    public Flux<DataBuffer> getBody() {

        if (!Objects.requireNonNull(getHeaders().getContentType()).equalsTypeAndSubtype(MimeType.valueOf("application/json"))) {
            log.info("Request: method={}, uri={}, headers={}, body=(not json)", getMethod(), super.getPath(), headers);
            return super.getBody();
        }

        return super.getBody()
                .collectList()
                .flatMapMany(dataBuffers -> {
                    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                        WritableByteChannel writableChannel = Channels.newChannel(baos);
                        for (DataBuffer buffer : dataBuffers) {
                            writableChannel.write(buffer.asByteBuffer().asReadOnlyBuffer());
                        }
                        writableChannel.close();
                        String body = baos.toString(UTF_8);
                        String payload = Strings.isNotBlank(body) ? body : "(empty)";
//                        log.info("Request: id={} method={}, uri={}, headers={}, payload={}", requestId, getDelegate().getMethod(), getDelegate().getPath(), headers, processBody(payload));
                        log.info("Request: method={}, uri={}, headers={}, payload={}", getDelegate().getMethod(), getDelegate().getPath(), headers, processBody(payload));
                    } catch (Exception e) {
                        log.error("Couldn't write to ByteArrayOutputStream: {}", e.getMessage(), e);
                    }
                    return Flux.fromIterable(dataBuffers);
                });
    }

    // Если строк в json-е больше 5ти, убираем переносы строк, складываем все в одну строку для лучшей читаемостей остальных логов
    private String processBody(String body) {
        int maxLines = 1;
        int trimLines = 5000;
        String[] lines = body.split("\r\n|\r|\n|\\\\r\\\\n");
        if (lines.length > trimLines) {
            log.debug("http body is trimmed to {} lines. Original length: {} lines", trimLines, lines.length);
            lines = Arrays.copyOfRange(lines, 0, trimLines);
        }
        if (lines.length > maxLines) {
            // If more than maxLines, condense into a single line
            return String.join(" ", lines).replaceAll("\\s+", " ");
        }
        return body;
    }

}
