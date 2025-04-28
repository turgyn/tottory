package kz.tottory.lib.webflux.infra;

import kz.tottory.lib.webflux.infra.filter.ReactiveLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebfluxInfraConfig {

    @Bean
    public ReactiveLoggingFilter reactiveLoggingFilter() {
        return new ReactiveLoggingFilter();
    }
}
