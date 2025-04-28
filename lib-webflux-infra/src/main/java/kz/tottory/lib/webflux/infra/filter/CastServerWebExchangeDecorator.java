package kz.tottory.lib.webflux.infra.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

@Slf4j
public class CastServerWebExchangeDecorator extends ServerWebExchangeDecorator {
    private final RequestLoggingDecorator requestLoggingDecorator;
    private final ResponseLoggingDecorator responseLoggingDecorator;

    public CastServerWebExchangeDecorator(ServerWebExchange delegate) {
        super(delegate);

        requestLoggingDecorator = new RequestLoggingDecorator(delegate.getRequest());
        responseLoggingDecorator = new ResponseLoggingDecorator(delegate.getResponse());
    }

    @Override
    public ServerHttpRequest getRequest() {
        return requestLoggingDecorator;
    }

    @Override
    public ServerHttpResponse getResponse() {
        return responseLoggingDecorator;
    }
}