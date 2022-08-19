package com.bot.sup;

import com.bot.sup.config.properties.TelegramProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TelegramProperties.class)
public class SupApplication { public static void main(String[] args) {
        SpringApplication.run(SupApplication.class, args);
    }
}
