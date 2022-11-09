package com.bot.sup;

import com.bot.sup.common.properties.TelegramProperties;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.common.properties.message.InstructorMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.common.properties.message.ScheduleMessageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties({TelegramProperties.class, ScheduleMessageProperties.class,
        InstructorMessageProperties.class, ActivityMessageProperties.class, MainMessageProperties.class})
public class SupApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SupApplication.class, args);
    }
}
