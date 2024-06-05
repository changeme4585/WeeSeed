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

    public List<AacCard> getAacCard(String childCode,String constructorId){
        return em.createQuery("SELECT m FROM AacCard m WHERE m.childCode =:childCode and m.constructorId =:constructorId ORDER BY m.creationTime DESC", AacCard.class)
                .setParameter("childCode", childCode)
                .setParameter("constructorId",constructorId)
                .getResultList();
    }

    public  List<videoCard> getVideoCard(String childId,String userId){
        return em.createQuery("SELECT m FROM videoCard m WHERE m.childId =:childId and m.userId =:userId ORDER BY m.creationTime DESC", videoCard.class)
                .setParameter("childId", childId)
                .setParameter("userId",userId)
                .getResultList();
    }

}
