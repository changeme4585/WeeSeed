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

    /*
        public static INDArray loadImage(String imagePath, int imageWidth, int imageHeight, int channels) throws IOException {
        NativeImageLoader loader = new NativeImageLoader(imageHeight, imageWidth, channels);
        INDArray image = loader.asMatrix(new File(imagePath));
        return resizeImage(image, channels, imageWidth, imageHeight);
     }
     */
    /* @Override
        public INDArray asMatrix(File f) throws IOException {
          try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))) {
            return asMatrix(bis);
          }
        }
        */

    public static INDArray loadImage(File file, int imageWidth, int imageHeight, int channels) throws IOException {
            NativeImageLoader loader = new NativeImageLoader(imageHeight, imageWidth, channels);
            INDArray image = loader.asMatrix(file);
        return resizeImage(image, channels, imageWidth, imageHeight);
    }

    private static INDArray resizeImage(INDArray image, int channels, int newWidth, int newHeight) {
        try {
            INDArray resizedImage = Nd4j.createUninitialized(image.size(0), channels, newWidth, newHeight);
            resizedImage = resizedImage.assign(image);
            return resizedImage;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error during image resizing", e);
        }
    }

    private static File convertToFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("upload", file.getOriginalFilename());
        file.transferTo(tempFile);
        return tempFile;
    }
}
