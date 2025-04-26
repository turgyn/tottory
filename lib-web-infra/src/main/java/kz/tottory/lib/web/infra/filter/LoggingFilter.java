package kz.tottory.lib.web.infra.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logRequest(wrappedRequest);
            logResponse(wrappedRequest, wrappedResponse, duration);
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String headers = Collections.list(request.getHeaderNames()).stream()
                .map(headerName -> headerName + ": " + request.getHeader(headerName))
                .collect(Collectors.joining(", "));

        String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);

        log.info("[REQUEST] {} {} Headers=[{}] Body=[{}]",
                request.getMethod(), request.getRequestURI(), headers, removeLineBreaks(requestBody));
    }

    private void logResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, long duration) {
        int status = response.getStatus();
        String responseBody = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        if (status >= 400)
            log.error("[RESPONSE][EXCEPTION] {} {} -> {} body={} ({} ms)",
                    request.getMethod(), request.getRequestURI(),
                    status, sanitizeResponseBody(removeLineBreaks(responseBody)), duration);
        else
            log.info("[RESPONSE] {} {} -> {} body={} ({} ms)",
                    request.getMethod(), request.getRequestURI(),
                    status, sanitizeResponseBody(removeLineBreaks(responseBody)), duration);
    }

    private String sanitizeResponseBody(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);

            if (root.isObject() && root.has("trace")) {
                ((ObjectNode) root).remove("trace");
            }

            return mapper.writeValueAsString(root);
        } catch (Exception e) {
            return responseBody;
        }
    }

    private static String removeLineBreaks(String msg) {
        return msg.replaceAll("[\\n\\r]", "");
    }
}
