package com.example.WeeSeed.service;


import com.example.WeeSeed.dto.AacDto;
import com.example.WeeSeed.dto.GrowthDto;
import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.entity.Growth;
import com.example.WeeSeed.entity.LearningDiary;
import com.example.WeeSeed.entity.videoCard;
import com.example.WeeSeed.repository.AacRepository;
import com.example.WeeSeed.repository.GrowthRepository;
import com.example.WeeSeed.repository.VideoCardRepository;
import lombok.RequiredArgsConstructor;
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

    public void clicklog(Long cardId,String cardType)
    {
        //선택한 카드의 클릭 횟수를 증가하는 로직 and 오늘의 학습 (중복 금지하면서 오늘 이전거는 다 날림)
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
        String formattedDate = now.format(formatter); //현재시간을 String 형으로
        String childId = "";
        int clickCnt = 0;
        if(cardType.equals("aac")){

            AacCard aacCard = aacRepository.getAacCard(cardId);
            childId = aacCard.getChildId();
            clickCnt = aacCard.getClickCnt();
            aacCard.updateClick();
            Growth growth = Growth.builder().
                    imageUrl(aacCard.getImageUrl()).
                    userId(aacCard.getConstructorId()).
                    cardName(aacCard.getCardName()).
                    color(aacCard.getColor()).
                    creationTime(formattedDate).
                    build();
            growthRepository.writeGrowth(growth);
        }
        else {
            videoCard videoCard = videoCardRepository.getVideoCard(cardId);
            videoCard.updateClick();
            childId = videoCard.getChildId();
            clickCnt = videoCard.getClickCnt();
            Growth growth = Growth.builder().
                    imageUrl(videoCard.getThumbnailUrl()).
                    userId(videoCard.getUserId()).
                    cardName(videoCard.getCardName()).
                    color(videoCard.getColor()).
                    creationTime(formattedDate).
                    build();
            growthRepository.writeGrowth(growth);
        }



        //중복된 데이터 들어가면 안됨
        List<LearningDiary> learningDiaries = growthRepository.getAllLearning();
        if(learningDiaries.size()!=0) {  //데이터가 존재하고
            String learningDate = growthRepository.compareLearningDate();
            if(!formattedDate.equals(learningDate)){ //과거의 데이터인 경우
                for(LearningDiary learningDiary:learningDiaries){
                    growthRepository.deleteLearning(learningDiary); //모든 데이터 초기화
                }
            }
        }

            List<LearningDiary> getLearningCard  = growthRepository.getLearningCard(cardId,cardType);
            if(getLearningCard.size() ==0){ //오늘 처음 학습한 카드이면 저장
                LearningDiary learningDiary = LearningDiary.builder().
                        cardType(cardType).
                        date(formattedDate).
                        clickCnt(0).
                        childId(childId).
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

    public void test(){
        growthRepository.getLearningCard(Long.valueOf(1),"skdfsd");
        growthRepository.compareLearningDate();
    }
}
