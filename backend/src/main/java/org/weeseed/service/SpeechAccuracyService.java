package org.weeseed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weeseed.entity.SpeechAccuracy;
import org.weeseed.repository.SpeechAccuracyRepository;

/**
 * 아동의 발음 정확도 검사 결과를 저장하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SpeechAccuracyService {
    private final SpeechAccuracyRepository speechAccuracyRepository;

    /**
     * 아동의 발음 정확도 검사 결과를 저장
     *
     * @param childCode 발음 정확도 검사를 수행한 아동 코드
     * @param cardName  발음 정확도 검사를 수행한 AAC 카드명
     * @param score     발음 정확도 검사 점수
     */
    public void saveResult(String childCode,String cardName,String score){
        SpeechAccuracy speechAccuracy  = SpeechAccuracy.builder().
                childId(childCode).
                cardName(cardName).
                score(score).
                build();
        speechAccuracyRepository.saveResult(speechAccuracy);
    }
}