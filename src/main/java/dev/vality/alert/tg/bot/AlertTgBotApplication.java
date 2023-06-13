package dev.vality.alert.tg.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class AlertTgBotApplication extends SpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlertTgBotApplication.class, args);
    }

}
