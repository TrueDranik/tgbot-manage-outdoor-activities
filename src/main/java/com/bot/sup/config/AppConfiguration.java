package com.bot.sup.config;

import com.bot.sup.api.telegram.handler.registration.instructor.InstructorMessageProcessor;
import com.bot.sup.common.enums.InstructorStateEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class AppConfiguration {
    @Bean
    public Map<InstructorStateEnum, InstructorMessageProcessor> instructorMessageProcessorMap(List<InstructorMessageProcessor> processors) {
        return processors.stream()
                .collect(Collectors.toMap(InstructorMessageProcessor::getCurrentState, Function.identity()));
    }
}
