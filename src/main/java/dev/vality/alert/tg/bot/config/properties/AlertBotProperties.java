package dev.vality.alert.tg.bot.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Data
@Configuration
@ConfigurationProperties(prefix = "bot")
public class AlertBotProperties {

    @NotNull
    private String token;
    @NotNull
    private String name;
    @NotNull
    private String chatId;
    private Integer cacheOptionsSec = 0;
}
