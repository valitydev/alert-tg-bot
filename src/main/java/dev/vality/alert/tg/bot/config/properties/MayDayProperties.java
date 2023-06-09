package dev.vality.alert.tg.bot.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "mayday")
@Validated
@Getter
@Setter
public class MayDayProperties {

    @NotNull
    private Resource url;
    @NotNull
    private int networkTimeout = 5000;
}
