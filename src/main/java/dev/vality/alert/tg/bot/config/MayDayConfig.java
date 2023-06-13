package dev.vality.alert.tg.bot.config;

import dev.vality.alert.tg.bot.config.properties.MayDayProperties;
import dev.vality.alerting.mayday.AlertingServiceSrv;
import dev.vality.woody.thrift.impl.http.THSpawnClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MayDayConfig {

    @Bean
    public AlertingServiceSrv.Iface mayDayClient(MayDayProperties properties) throws IOException {
        return new THSpawnClientBuilder()
                .withAddress(properties.getUrl().getURI())
                .withNetworkTimeout(properties.getNetworkTimeout())
                .build(AlertingServiceSrv.Iface.class);
    }
}
