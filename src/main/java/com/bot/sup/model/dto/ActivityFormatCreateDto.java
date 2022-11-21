package com.bot.sup.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "Формат активности")
public class ActivityFormatCreateDto {
    @Schema(title = "Именование формата", defaultValue = "Format")
    private String name;
    @Schema(title = "Доп. информация для формата", nullable = true, defaultValue = " ")
    private String description;
}
