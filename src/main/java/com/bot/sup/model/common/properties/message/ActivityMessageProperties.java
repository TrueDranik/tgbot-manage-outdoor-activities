package com.bot.sup.model.common.properties.message;

import com.bot.sup.model.common.properties.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
@Configuration
@Data
@ConfigurationProperties(prefix = "message.activity")
@PropertySource(value = "classpath:messages.yaml", factory = YamlPropertySourceFactory.class)
public class ActivityMessageProperties {
    String activities;
    String menuActivities;
    String listActivity;
    String addActivity;
    String deleteActivity;
    String registeredActivity;
    String emptyActivity;
    String inputActivityName;
    String inputActivityNameIsEmpty;
    String activityNameAlreadyTaken;
}
