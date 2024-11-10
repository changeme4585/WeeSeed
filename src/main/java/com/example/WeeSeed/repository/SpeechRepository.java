package com.example.WeeSeed.repository;


import com.example.WeeSeed.entity.SpeechAccuracy;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SpeechRepository {
    private  final EntityManager em;

    public void saveResult(SpeechAccuracy speechAccuracy){
        em.persist(speechAccuracy);
    }
}
