package com.example.WeeSeed.ImageAi;



import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
//        File file = new File(imageFile.getOriginalFilename());
//        imageFile.transferTo(file);
        INDArray arrayImg = loader.asMatrix(imageFile.getOriginalFilename());
        if (arrayImg == null)
            System.out.printf("---------------------arrayImg\n");
        return resizeImage(arrayImg, channels, imageWidth, imageHeight);
    }

    private static INDArray resizeImage(INDArray image, int channels, int newWidth, int newHeight) {
        INDArray resizedImage = Nd4j.createUninitialized(image.size(0), channels, newWidth, newHeight);
        resizedImage = resizedImage.assign(image);

        return resizedImage;
    }
}
