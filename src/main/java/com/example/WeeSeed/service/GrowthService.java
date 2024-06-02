package com.example.WeeSeed.service;


import com.example.WeeSeed.dto.AacDto;
import com.example.WeeSeed.dto.GrowthDto;
import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.entity.Growth;
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

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
        String formattedDate = now.format(formatter); //현재시간을 String 형으로

        if(cardType.equals("aac")){
            AacCard aacCard = aacRepository.getAacCard(cardId);
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


            Growth growth = Growth.builder().
                    imageUrl(videoCard.getThumbnailUrl()).
                    userId(videoCard.getUserId()).
                    cardName(videoCard.getCardName()).
                    color(videoCard.getColor()).
                    creationTime(formattedDate).
                    build();
            growthRepository.writeGrowth(growth);

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
}
