package com.bot.sup.common.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {
    private String botUsername;
    private String botToken;
    @Value("${telegram.url.constructor}")
    private String webAppConstructor;
    @Value("${telegram.url.activity}")
    private String webAppActivity;
    @Value("${telegram.url.updateActivity}")
    private String webAppUpdateActivity;
    @Value("${telegram.url.route}")
    private String webAppRoute;
    @Value("${telegram.url.updateRoute}")
    private String webAppUpdateRoute;
}
