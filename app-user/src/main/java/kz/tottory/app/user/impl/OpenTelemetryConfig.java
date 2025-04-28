package kz.tottory.app.user.impl;

//import io.opentelemetry.api.common.Attributes;
//import io.opentelemetry.sdk.OpenTelemetrySdk;
//import io.opentelemetry.sdk.resources.Resource;
//import io.opentelemetry.sdk.trace.SdkTracerProvider;
//import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTelemetryConfig {

//    @Bean
//    public OpenTelemetrySdk openTelemetrySdk() {
//        var loggingExporter = new LoggingSpanExporter();
//        var tracerProvider = SdkTracerProvider.builder()
//                .setResource(Resource.create(Attributes.builder()
//                        .put("service.name", "app-user")
//                        .build()))
//                .addSpanProcessor(SimpleSpanProcessor.create(loggingExporter))
//                .build();
//
//        return OpenTelemetrySdk.builder()
//                .setTracerProvider(tracerProvider)
//                .build();
//    }
}