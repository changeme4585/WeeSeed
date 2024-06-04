package com.example.WeeSeed.ImageAi;

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
        // ResNet 모델 로드 및 초기화 (사전 훈련된 가중치 사용)
        ZooModel<ComputationGraph> model = ResNet50.builder().build();
        ComputationGraph resNet50 = (ComputationGraph) model.initPretrained();

        // 이미지 스케일링 및 전처리
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0, 1);
        scaler.transform(image);
//edge 추출 코드 추가할 것 (여기서만 작용해야함)
        // 이미지에 대한 예측 수행
        INDArray[] output = resNet50.output(image);

        // 수정할 것
        // 첫 번째 클래스의 확률이 일정 값 이상이면 객체가 존재한다고 판단
        double objectProbabilityThreshold = 0.0010232;
        double objectProbability = output[0].getDouble(0);
        System.out.println("ObjectProbability : " + objectProbability);
        return objectProbability >= objectProbabilityThreshold;
    }

    public static boolean isBrightnessOutOfRange(INDArray image) {
        INDArray grayscaleImage = image.mean(2);
        double mean = grayscaleImage.meanNumber().doubleValue(); // 평균값 계산

        System.out.println("Mean : " + mean);

        /* 임계값 설정 */
        double brightThreshold = 0.9;
        double darkThreshold = 0.25;

        boolean isTooBright = mean > brightThreshold;
        boolean isTooDark = mean < darkThreshold;

        System.out.println("isTooBright : " + isTooBright);
        System.out.println("isTooDark : " + isTooDark);
        return isTooBright || isTooDark;
    }

}
