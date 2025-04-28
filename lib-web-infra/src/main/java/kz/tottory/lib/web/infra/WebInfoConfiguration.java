package kz.tottory.lib.web.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import kz.tottory.lib.web.infra.exception.GlobalExceptionHandler;
import kz.tottory.lib.web.infra.filter.LoggingFilter;
import kz.tottory.lib.web.infra.filter.TracingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebInfoConfiguration {

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler(objectMapper);
    }

//    not needed anymore, replaced by micrometer tracing
//    @Bean
//    public TracingFilter tracingFilter() { // tracingFilter have to be registered before loggingFilter
//        return new TracingFilter();
//    }

    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }

}
