package com.example.WeeSeed.service;


import com.example.WeeSeed.DefaultImageLoader;
import com.example.WeeSeed.dto.DefaultImageDto;
import com.example.WeeSeed.entity.DefaultImage;
import com.example.WeeSeed.repository.DefaultImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultImageService {
    private final DefaultImageRepository defaultImageRepository;

    @Value("${imagePath}") //기본 이미지 저장소 위치
    private String imagePath;
    //private static final String UPLOAD_DIR = "uploads/";
    public byte[] getImageData(String cardName) throws IOException {
        DefaultImageLoader imageLoader = new DefaultImageLoader(imagePath+cardName+".jpg");
        return imageLoader.loadImage();
    }

    //기본 이미지를 리턴
    public List<DefaultImageDto> getUserDefaultImageList(String constructorId) throws IOException {
        List<DefaultImage> defaultImageList =  defaultImageRepository.getUserDefaultImageList(constructorId);
        List<DefaultImageDto> defaultImageDtos = new ArrayList<>();

        for(DefaultImage defaultImage:defaultImageList){
            DefaultImageDto defaultImageDto = DefaultImageDto.builder().
                    cardName(defaultImage.getCardName()).
                    cardImage(getImageData(defaultImage.getCardName())).
                    build();
            defaultImageDtos.add(defaultImageDto);
        }
        return defaultImageDtos;
    }
    public  void deleteDefaultImage(String constructorId,String cardName){
        DefaultImage defaultImage = defaultImageRepository.getDeleteCard(constructorId,cardName);
        defaultImageRepository.deleteDefaultImage(defaultImage);
    }
}
