package com.bot.sup.model.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] imagedata;
}
