package com.example.WeeSeed.ImageAi;

import com.example.WeeSeed.WeeSeedApplication;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.ResNet50;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import java.io.IOException;

public class ImageChecker {
    public static boolean isSuitable(INDArray image) throws IOException {
        //객체 판단 및 명암 판단
        boolean isObjectPresent = isObjectPresent(image);
        boolean isOutOfRange = isBrightnessOutOfRange(image);

        boolean isSuitable = isObjectPresent && !isOutOfRange;

        return isSuitable;
    }

    public static boolean isObjectPresent(INDArray image) throws IOException {
        // Get the ResNet50 model and ComputationGraph from the AacService
        ComputationGraph resNet50 = WeeSeedApplication.resNet50;

        // Preprocess the image
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0, 1);
        scaler.transform(image);

        // Perform prediction on the image
        INDArray[] output = resNet50.output(image);

        // Set the object probability threshold
        double objectProbabilityThreshold = 0.0010232; // 0.00102311

        // Get the probability of the first class (assuming it represents the presence of an object)
        double objectProbability = output[0].getDouble(0);
        System.out.println("ObjectProbability : " + objectProbability);

        // Check if the object probability is above the threshold
        return objectProbability >= objectProbabilityThreshold;
    }

    public static boolean isBrightnessOutOfRange(INDArray image) {
        INDArray grayscaleImage = image.mean(2);
        double mean = grayscaleImage.meanNumber().doubleValue(); // 평균값 계산

        System.out.println("Mean : " + mean);

        /* 임계값 설정 */
        double brightThreshold = 0.9;
        double darkThreshold = 0.25;//합 //0.35

        boolean isTooBright = mean > brightThreshold;
        boolean isTooDark = mean < darkThreshold;

        System.out.println("isTooBright : " + isTooBright);
        System.out.println("isTooDark : " + isTooDark);
        return isTooBright || isTooDark;
    }

}
