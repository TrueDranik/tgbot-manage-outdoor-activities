package com.bot.sup.model;

import lombok.Data;

@Data
public class ActivityRequestParams {
    private Long activityFormatId;
    private Long activityTypeId;
    private Long routeId;
}
