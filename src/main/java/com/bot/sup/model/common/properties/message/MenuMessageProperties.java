package com.bot.sup.model.common.properties.message;

import com.bot.sup.model.common.properties.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@ConfigurationProperties(prefix = "message.menus")
@PropertySource(value = "classpath:messages.yaml", factory = YamlPropertySourceFactory.class)
public class MenuMessageProperties {
    String menu;
    String change;
    String delete;
    String back;
    String userChoose;
}
