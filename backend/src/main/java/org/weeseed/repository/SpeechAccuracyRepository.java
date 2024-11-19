package org.weeseed.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.weeseed.entity.SpeechAccuracy;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SpeechAccuracyRepository {
    private  final EntityManager em;

    /**
     * 발음 정확도 데이터를 저장
     *
     * @param speechAccuracy 저장할 발음 정확도 데이터
     */
    public void saveResult(SpeechAccuracy speechAccuracy){
        em.persist(speechAccuracy);
    }

    /**
     * 특정 아동의 발음 정확도 데이터 리스트 검색
     *
     * @param childId   조회할 아동의 ID
     * @return          아동의 발음 정확도 데이터 리스트
     */
    public List<SpeechAccuracy> getSpeechList(String childId){
        return  em.createQuery("SELECT m FROM SpeechAccuracy m WHERE m.childId =:childId", SpeechAccuracy.class).
                setParameter("childId",childId).
                getResultList();
    }
}