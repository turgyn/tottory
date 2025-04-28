package kz.tottory.app.balance;


import kz.tottory.lib.webflux.infra.WebfluxInfraConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@Import(WebfluxInfraConfig.class)
@Slf4j
public class BalanceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BalanceApplication.class);
        app.addListeners((ApplicationListener<ApplicationReadyEvent>) event -> {
            log.info("\n------------------------------------------------------------\n" +
                            "🎯 Service Started: {}\n" +
                            "🚀 Port: {}\n" +
                            "🛠️ Environment Profile: {}\n" +
                            "------------------------------------------------------------",
                    event.getApplicationContext().getEnvironment().getProperty("spring.application.name", "unknown"),
                    event.getApplicationContext().getEnvironment().getProperty("server.port", "unknown"),
                    event.getApplicationContext().getEnvironment().getProperty("spring.profiles.active", "default")
            );
        });
        app.run(args);
        Hooks.enableAutomaticContextPropagation();
    }

}