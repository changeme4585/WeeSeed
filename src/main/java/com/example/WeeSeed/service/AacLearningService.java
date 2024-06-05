package com.example.WeeSeed.service;

import com.example.WeeSeed.ImageAi.ImageLoader;

import com.example.WeeSeed.ImageAi.ImageDistanceCalculator;
import com.example.WeeSeed.dto.AacDto;
import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.repository.AacRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SecondaryRow;
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
        List<AacCard> aacCardList =aacRepository.getAllAacCard();
        // 이미지 파일 경로
        String label = "bag";
        int imageCount = aacCardList.size();
        String[] imagePaths = new String[imageCount];
        String originPath = "";

        for (int i = 0; i < imageCount; i++)
        {
            if( cardId ==aacCardList.get(i).getAacCardId()){
                originPath = aacCardList.get(i).getImageUrl();
            }else{
                imagePaths[i] = aacCardList.get(i).getImageUrl();
            }

        }



        // 이미지를 INDArray로 변환하여 저장
        Map<String, INDArray> imageMap = ImageLoader.loadImageMap(imagePaths, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_CHANNELS);

        // 원본 이미지를 INDArray로 변환
        INDArray originImage = ImageLoader.loadImage(originPath, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_CHANNELS);

        // 가장 유사한 이미지 두 개 찾기
        String mostSimilarImage1 = null;
        String mostSimilarImage2 = null;
        double lowestDistance = Double.MAX_VALUE;
        double secondLowestDistance = Double.MAX_VALUE;
        for (String imagePath : imagePaths) {
            INDArray image = imageMap.get(imagePath);
            double distance = ImageDistanceCalculator.calculateImageDistance(originImage, image);
            if (distance < lowestDistance) {
                secondLowestDistance = lowestDistance; // 두 번째로 낮은 거리를 갱신
                mostSimilarImage2 = mostSimilarImage1; // 두 번째로 낮은 거리의 이미지를 갱신
                lowestDistance = distance;
                mostSimilarImage1 = imagePath;
            } else if (distance < secondLowestDistance) {
                secondLowestDistance = distance; // 두 번째로 낮은 거리 갱신
                mostSimilarImage2 = imagePath;
            }
        }
       // return
    }
}


