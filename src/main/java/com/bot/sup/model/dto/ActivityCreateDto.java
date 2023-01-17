package com.bot.sup.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Schema(description = "Информация об активности")
public class ActivityCreateDto {
    @Schema(description = "Name activity")
    @Size(min = 0, max = 255)
    private String name;
    private String seasonality;
    private Long activityFormatId;
    private Long activityTypeId;
    private String description;
    private String duration;
    private String age;
    private String complexity;
    private BigDecimal price;
    private int prepayPercent;
}
