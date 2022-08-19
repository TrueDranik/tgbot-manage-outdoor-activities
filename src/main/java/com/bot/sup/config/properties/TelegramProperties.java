package com.bot.sup.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {
    private String nameBot;
    private String tokenBot;
    private String frontUrl;
}
