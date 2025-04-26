package kz.tottory.app.user;

import kz.tottory.lib.web.infra.WebInfoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(WebInfoConfiguration.class)
@Slf4j
public class AppUserApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AppUserApplication.class);
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
    }

}
