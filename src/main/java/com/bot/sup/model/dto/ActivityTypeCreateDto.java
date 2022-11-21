package com.bot.sup.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "Тип активности")
public class ActivityTypeCreateDto {
    @Schema(title = "Имя активности", defaultValue = "Type")
    private String name;
    @Schema(title = "Описание для типа активности", nullable = true, defaultValue = " ")
    private String description;
}
