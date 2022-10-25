package com.bot.sup.common.properties.message;

import com.bot.sup.common.properties.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@ConfigurationProperties(prefix = "message.main")
@PropertySource(value = "classpath:messages.yaml", factory = YamlPropertySourceFactory.class)
public class MainMessageProperties {
    String menu;
    String change;
    String delete;
    String back;
    String userChoose;
    String done;
    String commandStart;
    String commandStartDescription;
}
