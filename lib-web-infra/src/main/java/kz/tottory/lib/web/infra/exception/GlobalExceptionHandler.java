package kz.tottory.lib.web.infra.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;
//    @Value("${spring.profile.active}")
//    private String profile;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());

        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var code = status.value();
        if (ex instanceof ErrorResponseException ere) {
            code = ere.getStatusCode().value();
            status = HttpStatus.valueOf(code);
            body.replace("status", code);
            body.replace("error", status.getReasonPhrase());
        }
        if (ex instanceof ResponseStatusException rse) {
            body.replace("message", rse.getReason());
        }

        try {
            log.error("[RESPONSE][EXCEPTION] {} Body:{}", status, objectMapper.writeValueAsString(body), ex);
        } catch (JsonProcessingException jpe) {
            log.error("[RESPONSE][EXCEPTION] Unable to parse body. StackTrace={}", ex.getMessage(), jpe);
        }

//        log.error(profile);
//        if (!profile.equals("prod"))
            body.put("trace", getStackTraceAsString(ex));

        return ResponseEntity.status(status).body(body);
    }

    private String getStackTraceAsString(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
