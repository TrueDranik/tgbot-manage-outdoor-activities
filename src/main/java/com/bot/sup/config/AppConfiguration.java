package com.bot.sup.config;

import com.bot.sup.api.telegram.handler.registration.activity.format.states.ActivityFormatMessageProcessor;
import com.bot.sup.api.telegram.handler.registration.activity.type.states.ActivityTypeMessageProcessor;
import com.bot.sup.api.telegram.handler.registration.client.states.ClientRecordMessageProcessor;
import com.bot.sup.api.telegram.handler.registration.information.states.InformationAboutUsMessageProcessor;
import com.bot.sup.api.telegram.handler.registration.instructor.states.InstructorMessageProcessor;
import com.bot.sup.common.enums.states.*;
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

    @Bean
    public Map<ActivityFormatStateEnum, ActivityFormatMessageProcessor> activityFormatMessageProcessorMap(List<ActivityFormatMessageProcessor> processors) {
        return processors.stream()
                .collect(Collectors.toMap(ActivityFormatMessageProcessor::getCurrentState, Function.identity()));
    }

    @Bean
    public Map<ActivityTypeStateEnum, ActivityTypeMessageProcessor> activityTypeMessageProcessorMap(List<ActivityTypeMessageProcessor> processors) {
        return processors.stream()
                .collect(Collectors.toMap(ActivityTypeMessageProcessor::getCurrentState, Function.identity()));
    }

    @Bean
    public Map<InformationAboutUsStateEnum, InformationAboutUsMessageProcessor> abouUsMessageProcessorMap(List<InformationAboutUsMessageProcessor> processors) {
        return processors.stream()
                .collect(Collectors.toMap(InformationAboutUsMessageProcessor::getCurrentState, Function.identity()));
    }

    @Bean
    public Map<ClientRecordStateEnum, ClientRecordMessageProcessor> clientRecordMessageProcessorMap(List<ClientRecordMessageProcessor> processors) {
        return processors.stream()
                .collect(Collectors.toMap(ClientRecordMessageProcessor::getCurrentState, Function.identity()));
    }
}
