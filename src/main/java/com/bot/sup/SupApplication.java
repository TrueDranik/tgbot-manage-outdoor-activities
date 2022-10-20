package com.bot.sup;

import com.bot.sup.model.common.properties.TelegramProperties;
import com.bot.sup.model.common.properties.message.ActivityMessageProperties;
import com.bot.sup.model.common.properties.message.InstructorMessageProperties;
import com.bot.sup.model.common.properties.message.MainMessageProperties;
import com.bot.sup.model.common.properties.message.ScheduleMessageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({TelegramProperties.class, ScheduleMessageProperties.class,
        InstructorMessageProperties.class, ActivityMessageProperties.class, MainMessageProperties.class})
public class SupApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupApplication.class, args);
    }
}
