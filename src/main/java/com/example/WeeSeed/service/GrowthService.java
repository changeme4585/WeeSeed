package com.example.WeeSeed.service;


import com.example.WeeSeed.dto.AacDto;
import com.example.WeeSeed.dto.GrowthDiaryDto;
import com.example.WeeSeed.dto.GrowthDto;
import com.example.WeeSeed.dto.LearningDto;
import com.example.WeeSeed.entity.*;
import com.example.WeeSeed.repository.AacRepository;
import com.example.WeeSeed.repository.GrowthRepository;
import com.example.WeeSeed.repository.UserInfoRepository;
import com.example.WeeSeed.repository.VideoCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GrowthService {

    private final AacRepository aacRepository;
    private final VideoCardRepository videoCardRepository;

    private  final GrowthRepository growthRepository;

    private  final UserInfoRepository userInfoRepository;

    @Value("${raspberry.pi.url}")
    private String raspberryPiUrl;

    public void clicklog(Long cardId,String cardType)
    {
        //선택한 카드의 클릭 횟수를 증가하는 로직 and 오늘의 학습 (중복 금지하면서 오늘 이전거는 다 날림)
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
        String formattedDate = now.format(formatter); //현재시간을 String 형으로
        String childId = "";
        String userId = "";
        String color  = "";
        String  imageUrl = "";
        String cardName  = "";


        if(cardType.equals("aac")){

            AacCard aacCard = aacRepository.getAacCard(cardId);
            childId = aacCard.getChildId();
            userId = aacCard.getConstructorId();
            imageUrl = aacCard.getImageUrl();
            color = aacCard.getColor();
            cardName = aacCard.getCardName();
            aacCard.updateClick();
            Growth growth = Growth.builder().
                    imageUrl(aacCard.getImageUrl()).
                    userId(userId).
                    cardName(cardName).
                    color(color).
                    creationTime(formattedDate).
                    build();
            growthRepository.writeGrowth(growth);
        }
        else {
            videoCard videoCard = videoCardRepository.getVideoCard(cardId);
            videoCard.updateClick();
            childId = videoCard.getChildId();
            imageUrl = videoCard.getThumbnailUrl();
            cardName = videoCard.getCardName();
            color = videoCard.getColor();
            userId  = videoCard.getUserId();
            Growth growth = Growth.builder().
                    imageUrl(videoCard.getThumbnailUrl()).
                    userId(userId).
                    cardName(cardName).
                    color(color).
                    creationTime(formattedDate).
                    build();
            growthRepository.writeGrowth(growth);
        }

        //중복된 데이터 들어가면 안됨
//        List<LearningDiary> learningDiaries = growthRepository.getAllLearning();
//        if(learningDiaries.size()!=0) {  //데이터가 존재하고
//            String learningDate = growthRepository.compareLearningDate();
//            if(!formattedDate.equals(learningDate)){ //과거의 데이터인 경우
//                for(LearningDiary learningDiary:learningDiaries){
//                    growthRepository.deleteLearning(learningDiary); //모든 데이터 초기화
//                }
//            }
//        }
        List<LearningDiary> getLearningCard  = growthRepository.getLearningCard(cardId,cardType);
            //List<LearningDiary> getLearningCard  = growthRepository.getLearningCard(cardId,cardType);
            if(getLearningCard.size() ==0){ //오늘 처음 학습한 카드이면 저장
                LearningDiary learningDiary = LearningDiary.builder().
                        cardName(cardName).
                        cardId(cardId).
                        cardType(cardType).
                        date(formattedDate).
                        clickCnt(0).
                        childId(childId).
                        imageUrl(imageUrl).  //오늘 학습한 카드 이미지
                        color(color).
                        userId(userId).
                        build();
                growthRepository.writeLearning(learningDiary);
            }else{ //오늘 학습한적이 있는 카드이면 클릭횟수 증가
                getLearningCard.get(0).updateClick();
            }


    }

    public List<GrowthDto> getGrowthList(String creationTime,String userId){
        List<GrowthDto> growthDtoList = new ArrayList<>();
        List<Growth> growths = growthRepository.getGrowth(creationTime,userId);

        for(Growth growth : growths){
            GrowthDto growthDto = GrowthDto.builder().
                    image(growth.getImageUrl()).
                    cardName(growth.getCardName()).
                    color(growth.getColor()).
                    build();
            growthDtoList.add(growthDto);
        }
        return  growthDtoList;
    }

    public void makeGrowth(String userId,String childCode){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
        String creationTime = now.format(formatter); //현재시간을 String 형으로
        List<AacCard> aacCards = aacRepository.getTodayAacCardList(creationTime,userId);
        List<videoCard> videoCards = videoCardRepository.getTodayVideoCardList(creationTime,userId);
        List<LearningDiary> learningDiaries = growthRepository.getAllLearning();

        GrowthDiary growthDiary = GrowthDiary.builder().
                childCode(childCode).
                creationTime(creationTime).
                imageCardNum(aacCards.size()).
                videoCardNum(videoCards.size()).
                userId(userId).
                creationTime(creationTime).
                build();
        growthRepository.writeGrowthDiary(growthDiary);
    }
    public List<GrowthDiaryDto> getGrowthDiaryDto(String userId,String childId){
//        System.out.println("성장일지 불러오기");
//        System.out.println("creationTime :"+creationTime);
//        System.out.println("userId :"+userId);
//        System.out.println("childId :"+childId);
        List<GrowthDiaryDto> growthDiaryDtoList = new ArrayList<>();
        List<GrowthDiary> growthDiaryList = growthRepository.getGrowthDiary(userId,childId);

        for(GrowthDiary growthDiary:growthDiaryList){

            List<LearningDiary> learningDiaryList = growthRepository.getLearning(growthDiary.getCreationTime(),childId,userId);
            List<LearningDto> learningDtoList = new ArrayList<>();
            for(LearningDiary learningDiary : learningDiaryList){
                LearningDto learningDto  = LearningDto.builder().
                        image(raspberryPiUrl+learningDiary.getImageUrl()).
                        cardName(learningDiary.getCardName()).
                        color(learningDiary.getColor()).
                        build();
                learningDtoList.add(learningDto);
            }
            GrowthDiaryDto growthDiaryDto = GrowthDiaryDto.
                    builder().
                    imageCardNum(growthDiary.getImageCardNum()).
                    videoCardNum(growthDiary.getVideoCardNum()).
                    learningDtoList(learningDtoList).
                    creationTime(growthDiary.getCreationTime()).
                    userName(userInfoRepository.getUser(growthDiary.getUserId()).get(0).getName()).
                    build();
            growthDiaryDtoList.add(growthDiaryDto);
        }
        return growthDiaryDtoList;
    }


}
