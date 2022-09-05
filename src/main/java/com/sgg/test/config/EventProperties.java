package com.sgg.test.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Data
@Component
@ConfigurationProperties("app.event")
public class EventProperties {
    private Fine fine;

    @Data
    public static class Fine {
        @NotNull()
        private Integer speedRate;
        private Integer redLightRate;
    }
}