package com.bot.sup.common.properties.message;

import com.bot.sup.common.properties.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@Data
@ConfigurationProperties(prefix = "message.schedule")
@PropertySource(value = "classpath:messages.yaml", factory = YamlPropertySourceFactory.class)
public class ScheduleMessageProperties {
    String schedules;
    String menuSchedules;
}