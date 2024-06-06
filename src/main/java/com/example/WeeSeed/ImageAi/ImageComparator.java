package com.example.WeeSeed.ImageAi;

;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.IOException;
import java.util.Map;

public class ImageComparator {
    private static final int IMAGE_WIDTH = 28;
    private static final int IMAGE_HEIGHT = 28;
    private static final int IMAGE_CHANNELS = 3;

    /* Simple Check
     * - bag => {1, 2, 3, 4}와 oringBag 비교 => bag1와 유사, bag2와 유사
     * - bag => {1, 2, 3, 4}와 oringBag2 비교 => bag4와 같은 사진 (못 찾음), bag2와 유사, bag3과 유사
     * - mug => {1, 2, 3, 4}와 oringMug 비교 => mug1와 같은 사진, mug4와 유사
     * - mug => {1, 2, 3, 4}와 orinnMug2 비교 => mug1와 유사, mug4와 유사
     * */

    public static void main(String[] args) throws IOException {

        // 이미지 파일 경로
        String label = "bag";
        int imageCount = 4;
        String[] imagePaths = new String[imageCount];
        for (int i = 0; i < imageCount; i++)
            imagePaths[i] = "src/main/resources/images/" + label + (i + 1) + ".jpg";

        // 원본 이미지 경로
       // String originPath = "src/main/resources/images/originBag2.jpg";

        // 이미지를 INDArray로 변환하여 저장
//        Map<String, INDArray> imageMap = ImageLoader.loadImageMap(imagePaths, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_CHANNELS);

        // 원본 이미지를 INDArray로 변환
//        INDArray originImage = ImageLoader.loadImage(originPath, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_CHANNELS);
//
//        // 가장 유사한 이미지 두 개 찾기
//        String mostSimilarImage1 = null;
//        String mostSimilarImage2 = null;
//        double lowestDistance = Double.MAX_VALUE;
//        double secondLowestDistance = Double.MAX_VALUE;
//        for (String imagePath : imagePaths) {
//            INDArray image = imageMap.get(imagePath);
//            double distance = ImageDistanceCalculator.calculateImageDistance(originImage, image);
//            if (distance < lowestDistance) {
//                secondLowestDistance = lowestDistance; // 두 번째로 낮은 거리를 갱신
//                mostSimilarImage2 = mostSimilarImage1; // 두 번째로 낮은 거리의 이미지를 갱신
//                lowestDistance = distance;
//                mostSimilarImage1 = imagePath;
//            } else if (distance < secondLowestDistance) {
//                secondLowestDistance = distance; // 두 번째로 낮은 거리 갱신
//                mostSimilarImage2 = imagePath;
//            }
//        }

        // 결과 출력
//        System.out.println("Most similar images:");
//        System.out.printf("Image 1: %s | Distance: %f\n", mostSimilarImage1.substring(mostSimilarImage1.lastIndexOf('/') + 1), lowestDistance);
//        System.out.printf("Image 2: %s | Distance: %f\n", mostSimilarImage2.substring(mostSimilarImage2.lastIndexOf('/') + 1), secondLowestDistance);
    }
}

