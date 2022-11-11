package com.bot.sup.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Информация об активности")
public class ActivityCreateDto {
    @Schema(description = "Id activity")
    private Long id;
    @Schema(description = "Name activity")
    private String name;
    private String seasonality;
    private Long activityFormatId;
    private Long activityTypeId;
    private String description;
    private Long routeId;
    private String duration;
    private String age;
    private String complexity;
    private BigDecimal price;
}
