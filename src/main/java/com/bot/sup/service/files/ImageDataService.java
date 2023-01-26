package com.bot.sup.service.files;

import com.bot.sup.model.dto.ImageDataCreateDto;
import com.bot.sup.model.entity.ImageData;
import com.bot.sup.repository.ImageDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import util.ImageUtil;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageDataService {
    private final ImageDataRepository imageDataRepository;

    public ImageData findByRouteId(Long routeId) {
        return imageDataRepository.findByRouteId(routeId)
                .orElseThrow(() -> new EntityNotFoundException("Image for route id[%s] not found".formatted(routeId)));
    }

    public ImageDataCreateDto uploadImage(MultipartFile file) throws IOException {
        ImageData imageData = new ImageData();
        try (BufferedInputStream inputStream = new BufferedInputStream(file.getInputStream())) {
            imageData.setName(file.getOriginalFilename());
            imageData.setType(file.getContentType());
            imageData.setImageData(inputStream.readAllBytes());
        }

        imageDataRepository.save(imageData);
        return ImageDataCreateDto.builder()
                .id(imageData.getId())
                .name(imageData.getName())
                .type(imageData.getType())
                .build();
    }

    @Transactional
    public ImageData getInfoByImageByName(String name) {
        ImageData dbImage = findByName(name);

        return ImageData.builder()
                .id(dbImage.getId())
                .name(dbImage.getName())
                .type(dbImage.getType())
                .imageData(dbImage.getImageData()).build();
    }

    public ImageData findByName(String name) {
        return imageDataRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Image with name[%s] not found".formatted(name)));
    }

    @Transactional
    public byte[] getImage(String name) {
        ImageData dbImage = findByName(name);
        return ImageUtil.decompressImage(dbImage.getImageData());
    }

    @Transactional
    public ImageData findImageById(Long id) {
        return imageDataRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Image with id - [%s] found".formatted(id)));
    }
}
