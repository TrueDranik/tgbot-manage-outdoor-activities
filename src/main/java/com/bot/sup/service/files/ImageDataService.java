package com.bot.sup.service.files;

import com.bot.sup.model.entity.ImageData;
import com.bot.sup.repository.ImageDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import util.ImageUtil;

import javax.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageDataService {
    private final ImageDataRepository imageDataRepository;

    public void uploadImage(MultipartFile file) throws IOException {
        ImageData imageData = new ImageData();
        try (var inputStream = new BufferedInputStream(file.getInputStream())) {
            imageData.setName(file.getOriginalFilename());
            imageData.setType(file.getContentType());
            imageData.setImageData(inputStream.readAllBytes());
        }
        imageDataRepository.save(imageData);
    }

    @Transactional
    public ImageData getInfoByImageByName(String name) {
        Optional<ImageData> dbImage = imageDataRepository.findByName(name);

        return ImageData.builder()
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .imageData(dbImage.get().getImageData()).build();

    }

    @Transactional
    public byte[] getImage(String name) {
        Optional<ImageData> dbImage = imageDataRepository.findByName(name);
        return ImageUtil.decompressImage(dbImage.get().getImageData());
    }
}
