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
    String notFoundFormat;
    String dateChoice;
    String confirmCancelReservationClient;
    String cancelReservationClientDone;
    String invitedFriends;
    String connect;
    String cancelReservation;
    String notFoundDate;
    String chooseRouteForDate;
    String clientsRecorded;
    String recordClient;
    String confirmCancelEvent;
    String eventCancelled;
    String tourEditor;
    String clientInformation;
    String fullDescription;
    String changeSchedule;
    String cancelSchedule;
    String changeDeleteRoute;
    String addNewRoute;
    String makeSchedule;
    String createActivity;
    String changeActivity;
}