package com.example.WeeSeed.repository;


import com.example.WeeSeed.entity.SpeechAccuracy;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SpeechRepository {
    private  final EntityManager em;

    public void saveResult(SpeechAccuracy speechAccuracy){
        em.persist(speechAccuracy);
    }
    public List<SpeechAccuracy> getSpeechList(String childCode){
        return  em.createQuery("select m from SpeechAccuracy m where m.childCode =:childCode").
                setParameter("childCode",childCode).
                getResultList();
    }
}
