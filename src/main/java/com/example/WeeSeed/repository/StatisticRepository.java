package com.example.WeeSeed.repository;


import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.entity.videoCard;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatisticRepository {
    private final EntityManager em;

//    public List<AacCard> getAacCardGrade(String grade) {
//        return em.createQuery("SELECT a FROM AacCard a " +
//                        "JOIN Child c ON a.childId = c.childCode WHERE c.gender =: gender", AacCard.class)
//                .setParameter("gender",gender)
//                .getResultList();
//    }
    public List<AacCard> getAaCardGender(String gender){  //성별 기준으로
       return em.createQuery("SELECT a FROM AacCard a " +
               "JOIN Child c ON a.childId = c.childCode WHERE c.gender =: gender", AacCard.class)
               .setParameter("gender",gender)
               .getResultList();
    }

    public List<videoCard> getVideoCardGender(String gender){
        return em.createQuery("SELECT v FROM videoCard v JOIN Child c ON v.childId = c.childCode WHERE c.gender = :gender", videoCard.class)
                .setParameter("gender", gender)
                .getResultList();
    }
    public List<AacCard> getAacCard(String childId,String constructorId){
        return em.createQuery("SELECT m FROM AacCard m WHERE m.childId =:childId and m.constructorId =:constructorId ORDER BY m.creationTime DESC", AacCard.class)
                .setParameter("childId", childId)
                .setParameter("constructorId",constructorId)
                .getResultList();
    }

    public  List<videoCard> getVideoCard(String childId,String userId){
        return em.createQuery("SELECT m FROM videoCard m WHERE m.childId =:childId and m.userId =:userId ORDER BY m.creationTime DESC", videoCard.class)
                .setParameter("childId", childId)
                .setParameter("userId",userId)
                .getResultList();
    }

    public Long getTypeNum(String disabilityType){
        return  em.createQuery("SELECT COUNT(c) FROM Child c WHERE c.disabilityType =:disabilityType",Long.class).
                setParameter("disabilityType",disabilityType).
                getSingleResult();
    }
    public Long getGradeNum(String grade){
        return  em.createQuery("SELECT COUNT(c) FROM Child c WHERE c.grade =:grade",Long.class).
                setParameter("grade",grade).
                getSingleResult();
    }
}
