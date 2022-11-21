package com.bot.sup.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "Тип активности")
public class ActivityTypeCreateDto {
    @Schema(title = "Именование типа", defaultValue = "Type")
    private String name;
    @Schema(title = "Доп. информация для типа", nullable = true, defaultValue = " ")
    private String description;
}
