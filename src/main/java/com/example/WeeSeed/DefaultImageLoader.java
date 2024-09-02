package com.example.WeeSeed;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultImageLoader {
    private String imageUrl;
    public DefaultImageLoader(String imageUrl){
        this.imageUrl = imageUrl;
    }
    public byte[] loadImage() throws IOException {

        // 리소스 파일을 불러옵니다.
        Path path = Paths.get(imageUrl);
        return Files.readAllBytes(path);
    }
}