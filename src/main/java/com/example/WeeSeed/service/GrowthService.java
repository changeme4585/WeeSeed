package com.example.WeeSeed.service;


import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.entity.videoCard;
import com.example.WeeSeed.repository.AacRepository;
import com.example.WeeSeed.repository.VideoCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GrowthService {

    private final AacRepository aacRepository;
    private final VideoCardRepository videoCardRepository;

    public void clicklog(Long cardId,String cardType)
    {
        if(cardType.equals("aac")){
            AacCard aacCard = aacRepository.getAacCard(cardId);
            aacCard.updateClick();
        }
        else {
            videoCard videoCard = videoCardRepository.getVideoCard(cardId);
            videoCard.updateClick();;
        }
    }
}
