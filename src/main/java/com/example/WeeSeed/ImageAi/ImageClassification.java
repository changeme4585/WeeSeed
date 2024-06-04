package com.example.WeeSeed.ImageAi;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ImageClassification {
    private static final int IMAGE_WIDTH = 28;
    private static final int IMAGE_HEIGHT = 28;
    private static final int IMAGE_CHANNELS = 3;

    public static void main(String[] args) throws IOException {
//        String imagePath = "src/main/resources/images/noObject.png"; // abnormal : mean 1.0 objP      0.001023138058371842
//        String imagePath = "src/main/resources/images/black.png"; // abnormal : mean 0.05 objP        0.0010226968443021178
//        String imagePath = "src/main/resources/images/grey.png"; // abnormal : mean 0.24 objP         0.001022770768031478
//        String imagePath = "src/main/resources/images/originBag.jpg";   // normal : mean 0.4774 objP  0.0010232054628431797
//        String imagePath = "src/main/resources/images/originMug.jpg";   // normal : mean 0.63328 objP 0.0010235009249299765
        String imagePath = "src/main/resources/images/mug3.jpg";   // normal : mean 0.63328 objP        0.0010229418985545635
       // MultipartFile imageFile = ;


    }
}
