package com.example.WeeSeed.service;


import com.example.WeeSeed.dto.AacDto;
import com.example.WeeSeed.dto.StatisticDto;
import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.entity.videoCard;
import com.example.WeeSeed.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticRepository statisticRepository;
    public StatisticDto getDateStatistic(int num,String childId,String userId){
        List<AacCard> aacCardList =statisticRepository.getAacCard(childId,userId);
        List<videoCard> videoCardList = statisticRepository.getVideoCard(childId,userId);
        int aacIdx = 0 ;
        int videoIdx = 0 ;
        int imageCardNum = aacCardList.size();
        int videoCardNum = videoCardList.size();
        String cardName = "";
        String image = "";
        String color = "";
        int maxCnt  = 0 ;
        String aacDate = "";
        String videoDate = "";
        if(imageCardNum != 0) {
            aacDate = aacCardList.get(0).getCreationTime();
        }
        if(videoCardNum != 0) {
            videoDate = videoCardList.get(0).getCreationTime();
        }

        int aacDateCnt = 0;
        int videoDateCnt = 0;
        while (aacIdx < aacCardList.size()  && aacDateCnt < num){
            if(maxCnt <aacCardList.get(aacIdx).getClickCnt()){
                cardName = aacCardList.get(aacIdx).getCardName();
                image = aacCardList.get(aacIdx).getImageUrl() ;
                color = aacCardList.get(aacIdx).getColor();
                maxCnt = aacCardList.get(aacIdx).getClickCnt();
            }


            if(!aacDate.equals(aacCardList.get(aacIdx).getCreationTime()) ){
                aacDate = aacCardList.get(aacIdx).getCreationTime();
                aacDateCnt+=1;
            }
            aacIdx+=1;
        }
        while(videoIdx < videoCardList.size() && videoDateCnt < num){
            if(maxCnt < videoCardList.get(videoIdx).getClickCnt()){
                cardName = videoCardList.get(videoIdx).getCardName();
                image = videoCardList.get(videoIdx).getThumbnailUrl();
                color = videoCardList.get(videoIdx).getColor();
                maxCnt = videoCardList.get(videoIdx).getClickCnt();
            }

            if(!videoDate.equals(videoCardList.get(videoIdx).getCreationTime())){
                videoDate = videoCardList.get(videoIdx).getCreationTime();
                videoDateCnt+=1;
            }
            videoIdx+=1;
        }

        StatisticDto statisticDto = StatisticDto.builder().
                imageCardNum(imageCardNum).
                videoCardNum(videoCardNum).
                cardName(cardName).
                image(image).
                color(color).
                build();

        return statisticDto;
    }
    public List<StatisticDto> getPersonalStatistic(String childId,String userId){
        List<StatisticDto> statisticDtoList = new ArrayList<>();
        statisticDtoList.add(getDateStatistic(1,childId,userId));
        statisticDtoList.add(getDateStatistic(7,childId,userId));
        statisticDtoList.add(getDateStatistic(30,childId,userId));
        return statisticDtoList;
    }
}
