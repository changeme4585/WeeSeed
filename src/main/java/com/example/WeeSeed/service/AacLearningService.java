package com.example.WeeSeed.service;

import com.example.WeeSeed.ImageAi.ImageLoader;

import com.example.WeeSeed.ImageAi.ImageDistanceCalculator;
import com.example.WeeSeed.dto.ExtendedCardDto;
import com.example.WeeSeed.entity.ExtendedCard;
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
    @Value("${raspberry.pi.url}")
    private String raspberryPiUrl;

//    public void findSimilarImage(Long cardId, String childCode) throws IOException {
//        List<AacCard> aacCardList = aacRepository.getNokAacCardList(childCode);
//        // 아동에 연결된 카드
//
//        // 이미지 파일 경로
//        String label = "bag";
//        int imageCount = aacCardList.size();
//        System.out.println(imageCount);
//        String[] imagePaths = new String[imageCount];
//        String originPath = "";
//        double distance = 0;
//
//
//        for (int i = 0; i < aacCardList.size(); i++) {
//            if (cardId.equals(aacCardList.get(i).getAacCardId())) {
//                originPath = aacCardList.get(i).getImageUrl();
//                System.out.println(i + " : " + originPath);
//                break;
//            }
//        }
//        if (originPath == null)
//            throw new IllegalArgumentException("카드 ID에 해당하는 이미지를 찾을 수 없습니다: " + cardId);
//
//        for (int i = 0; i < aacCardList.size(); i++) {
//            if (!cardId.equals(aacCardList.get(i).getAacCardId())) {
//                imagePaths[i] = aacCardList.get(i).getImageUrl();
//                System.out.println(i + " " + imagePaths[i]);
//                i++;
//            }
//        }
//
//        // 이미지를 INDArray로 변환하여 저장
//        Map<String, INDArray> imageMap = ImageLoader.loadImageMap(imagePaths, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_CHANNELS);
//
//        // 원본 이미지를 INDArray로 변환
////        INDArray originImage = ImageLoader.loadImage(originPath, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_CHANNELS);
//
//        // 가장 유사한 이미지 두 개 찾기
//        String mostSimilarImage = null;
//        double lowestDistance = Double.MAX_VALUE;
//        for (String imagePath : imagePaths) {
//            INDArray image = imageMap.get(imagePath);
//            distance = ImageDistanceCalculator.calculateImageDistance(originImage, image);
//            if (distance < lowestDistance) {
//                lowestDistance = distance;
//                mostSimilarImage = imagePath;
//            }
//        }
//        System.out.println(mostSimilarImage);
//        System.out.println("distance : " + distance);
//        System.out.println("imagePath : " + mostSimilarImage);
//        retrieveSimilars(cardId, mostSimilarImage, 0);
//
//    }
//    public void retrieveSimilars(Long repId, String imgUrl, int seqNum) {
//        ExtendedCard extendedCard = ExtendedCard.builder()
//                .representativeCardId(repId)
//                .imageUrl(imgUrl)
//                .sequence(seqNum + 1)
//                .build();
//// AACService에서, ExtendedCardDtoList 반환 해야함
//        extendedCardDtoRepository.save(extendedCard);
//    }
}

