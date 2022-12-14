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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageDataService {
    private final ImageDataRepository imageDataRepository;

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
        Optional<ImageData> dbImage = imageDataRepository.findByName(name);

        return ImageData.builder()
                .id(dbImage.get().getId())
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .imageData(dbImage.get().getImageData()).build();

    }

    @Transactional
    public byte[] getImage(String name) throws IOException {
        Optional<ImageData> dbImage = imageDataRepository.findByName(name);
        return ImageUtil.decompressImage(dbImage.get().getImageData());
    }

    @Transactional
    public ImageData getImageById(Long id) throws IOException {
        Optional<ImageData> dbImage = imageDataRepository.findById(id);
        return dbImage.orElseThrow(() -> new EntityNotFoundException("Image with id - [%s] found".formatted(id)));
    }
}
