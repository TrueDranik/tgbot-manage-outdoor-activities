package com.bot.sup.api.rest;

import com.bot.sup.model.dto.ImageDataCreateDto;
import com.bot.sup.model.entity.ImageData;
import com.bot.sup.service.files.ImageDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageDataController {
    private final ImageDataService imageDataService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageDataCreateDto uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        return imageDataService.uploadImage(file);
    }

    @GetMapping("/info/{name}")
    public ImageData getImageInfoByName(@PathVariable("name") String name) {
        return imageDataService.getInfoByImageByName(name);
    }

    @GetMapping("/{name}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable("name") String name) throws IOException {
        byte[] image = imageDataService.getImage(name);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable Long id) throws IOException {
        ImageData image = imageDataService.getImageById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(image.getName()));

        ByteArrayResource resource = new ByteArrayResource(image.getImageData());

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(image.getType()))
                .body(resource);
    }
}
