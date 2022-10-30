package com.bot.sup.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@Data
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {
    private String nameBot;
    private String tokenBot;
    private String frontUrl;
}
