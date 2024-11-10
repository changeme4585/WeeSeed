package com.example.WeeSeed.service;


import com.example.WeeSeed.entity.SpeechAccuracy;
import com.example.WeeSeed.repository.SpeechRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SpeechService {
    private final SpeechRepository speechRepository;
    public void saveResult(String childId,String cardName,String score){
        SpeechAccuracy speechAccuracy  = SpeechAccuracy.builder().
                childId(childId).
                cardName(cardName).
                score(score).
                build();
        speechRepository.saveResult(speechAccuracy);
    }
}
