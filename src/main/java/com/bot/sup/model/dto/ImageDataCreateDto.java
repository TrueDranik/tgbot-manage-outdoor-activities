package com.bot.sup.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ImageDataCreateDto {
    private Long id;

    private String name;

    private String type;

}
