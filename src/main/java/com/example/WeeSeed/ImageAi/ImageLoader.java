package com.example.WeeSeed.ImageAi;



import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class ImageLoader {
//    @Value("${raspberry.pi.url}")
//    private String raspberryPiUrl;


//    public static Map<String, INDArray> loadImageMap(String[] imagePaths, int imageWidth, int imageHeight, int channels) throws IOException {
//        Map<String, INDArray> imageMap = new HashMap<>();
//        NativeImageLoader loader = new NativeImageLoader(imageHeight, imageWidth, channels);
//
//        for (String imagePath : imagePaths) {
//            URL url = new URL("http://165.229.125.93:8000/" + imagePath);
//            BufferedImage bufferedImage = ImageIO.read(url);
//            if (bufferedImage == null)
//                throw new IOException("Wrong buffered Image");
//            File tempFile = File.createTempFile("temp_image", "jpg");
//            ImageIO.write(bufferedImage, "jpg", tempFile);
//
//            // 임시 파일에서 이미지를 INDArray로 변환하여 저장
//            INDArray image = loader.asMatrix(tempFile);
//            imageMap.put(imagePath, image);
//
//            // 임시 파일 삭제
//            tempFile.delete();
//        }
//        return imageMap;
//    }
//    public static INDArray loadImage(String imagePath, int imageWidth, int imageHeight, int channels) throws IOException {
//        NativeImageLoader loader = new NativeImageLoader(imageHeight, imageWidth, channels);
//        URL url = new URL("http://165.229.125.93:8000/" + imagePath);
//        File imageFile = new File(url.getFile());
//        INDArray image = loader.asMatrix(imageFile);
//        return resizeImage(image, channels, imageWidth, imageHeight);
//    }
//    public static INDArray loadImage(byte[] imageBytes, int imageWidth, int imageHeight, int channels) throws IOException {
//        NativeImageLoader loader = new NativeImageLoader(imageHeight, imageWidth, channels);
//        INDArray image = loader.asMatrix(new ByteArrayInputStream(imageBytes));
//        return image;
//    }

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
}
