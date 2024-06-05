package com.example.WeeSeed.service;

import com.example.WeeSeed.ImageAi.ImageLoader;

import com.example.WeeSeed.ImageAi.ImageDistanceCalculator;
import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.repository.AacRepository;
import lombok.RequiredArgsConstructor;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.nd4j.linalg.factory.Nd4j.image;

@Transactional
@Service
@RequiredArgsConstructor
public class AacLearningService {
    private final AacRepository aacRepository;
    private static final int IMAGE_WIDTH = 28;
    private static final int IMAGE_HEIGHT = 28;
    private static final int IMAGE_CHANNELS = 3;
    public void findSimilarImage (Long cardId,String childCode) throws IOException {
        List<AacCard> aacCardList = aacRepository.getAllAacCard();
        // 이미지 파일 경로
        String label = "bag";
        int imageCount = aacCardList.size();
        String[] imagePaths = new String[imageCount];
        String originPath = "";
        double distance = 0;
        System.out.println(label);

        for (int i = 0; i < imageCount; i++) {
            if (cardId == aacCardList.get(i).getAacCardId()) {
                originPath = aacCardList.get(i).getImageUrl();
            } else {
                //보호자 : 자기꺼 + 아동에 연결된 모든 재활사
                //재활사 : 자기꺼 + 아동에 연결된 보호자
                imagePaths[i] = aacCardList.get(i).getImageUrl();
            }
        }
        System.out.println("get imagePath[0] : " + imagePaths[0]);
        // 이미지를 INDArray로 변환하여 저장
        Map<String, INDArray> imageMap = ImageLoader.loadImageMap(imagePaths, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_CHANNELS);

        // 원본 이미지를 INDArray로 변환
        INDArray originImage = ImageLoader.loadImage(originPath, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_CHANNELS);

        // 가장 유사한 이미지 두 개 찾기
        String mostSimilarImage = null;
        double lowestDistance = Double.MAX_VALUE;
        for (String imagePath : imagePaths) {
            INDArray image = imageMap.get(imagePath);
            distance = ImageDistanceCalculator.calculateImageDistance(originImage, image);
            if (distance < lowestDistance) {
                lowestDistance = distance;
                mostSimilarImage = imagePath;
            }
        }
        System.out.println("distance : " + distance);
        System.out.println("imagePath : " + mostSimilarImage);

        // return
    }
}


