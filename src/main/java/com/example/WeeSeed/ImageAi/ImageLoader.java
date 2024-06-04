package com.example.WeeSeed.ImageAi;



import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    public static Map<String, INDArray> loadImageMap(String[] imagePaths, int imageWidth, int imageHeight, int channels) throws IOException {
        Map<String, INDArray> imageMap = new HashMap<>();
        NativeImageLoader loader = new NativeImageLoader(imageHeight, imageWidth, channels);

        for (String imagePath : imagePaths) {
            INDArray image = loader.asMatrix(new File(imagePath));
            imageMap.put(imagePath, resizeImage(image, channels, imageWidth, imageHeight));
        }

        return imageMap;
    }

    public static INDArray loadImage(MultipartFile imageFile, int imageWidth, int imageHeight, int channels) throws IOException {
        NativeImageLoader loader = new NativeImageLoader(imageHeight, imageWidth, channels);
        INDArray image = loader.asMatrix(imageFile);
        return resizeImage(image, channels, imageWidth, imageHeight);
    }

    private static INDArray resizeImage(INDArray image, int channels, int newWidth, int newHeight) {
        INDArray resizedImage = Nd4j.createUninitialized(image.size(0), channels, newWidth, newHeight);
        resizedImage = resizedImage.assign(image);

        return resizedImage;
    }
}
