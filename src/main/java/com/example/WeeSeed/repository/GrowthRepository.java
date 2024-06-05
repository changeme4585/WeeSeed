package com.example.WeeSeed.repository;


import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.entity.Growth;
import com.example.WeeSeed.entity.GrowthDiary;
import com.example.WeeSeed.entity.LearningDiary;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GrowthRepository {
    private final EntityManager em;
    public  void writeGrowth(Growth growth){
        em.persist(growth);
    }

    public  void writeGrowthDiary(GrowthDiary growthDiary) {em.persist(growthDiary);}
    public List<Growth> getGrowth(String creationTime,String userId){
        return em.createQuery("SELECT m FROM Growth m WHERE m.creationTime =:creationTime and m.userId =:userId ", Growth.class)
                .setParameter("creationTime", creationTime)
                .setParameter("userId",userId)
                .getResultList();
    }


    public List<GrowthDiary> getGrowthDiary( String userId, String childCode) {
        //
        return em.createQuery("SELECT m FROM GrowthDiary m WHERE m.userId =:userId and m.childCode =:childCode", GrowthDiary.class)
                //.setParameter("creationTime", creationTime)
                .setParameter("userId", userId)
                .setParameter("childCode", childCode)
                .getResultList();
    }

    public void writeLearning(LearningDiary learningDiary){
        em.persist(learningDiary);
    }

    public List<LearningDiary> getAllLearning(){
        return em.createQuery("SELECT m FROM LearningDiary m",LearningDiary.class).getResultList();
    }

    public String compareLearningDate(){  //
        return em.createQuery("SELECT m FROM LearningDiary m",LearningDiary.class).getResultList().get(0).getDate();
    }
    public List<LearningDiary> getLearningCard(Long cardId,String cardType){
        return em.createQuery("SELECT m FROM LearningDiary m WHERE m.cardId =:cardId and m.cardType =:cardType ",LearningDiary.class).
                setParameter("cardId",cardId).
                setParameter("cardType",cardType).
                getResultList();
    }
    public void deleteLearning(LearningDiary learningDiary){ em.remove(learningDiary);}

    public  List<LearningDiary> getLearning(String date,String childId,String userId) {
        return em.createQuery("SELECT m FROM LearningDiary m WHERE m.childId =:childId and m.userId =: userId and m.date =:date",LearningDiary.class).
                setParameter("childId",childId).
                setParameter("userId",userId).
                setParameter("date",date).
                getResultList();
    }
}
