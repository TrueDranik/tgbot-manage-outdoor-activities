package com.bot.sup.common.properties.message;

import com.bot.sup.common.properties.YamlPropertySourceFactory;
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
    String activityFormat;
    String activityType;
    String menuActivities;
    String listActivityFormat;
    String listActivityType;
    String addActivityFormat;
    String addActivityType;
    String deleteActivity;
    String registeredActivity;
    String emptyActivity;
    String inputActivityFormatName;
    String inputActivityNameIsEmpty;
    String activityNameAlreadyTaken;
}
