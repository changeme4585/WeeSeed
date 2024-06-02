package com.example.WeeSeed.repository;


import com.example.WeeSeed.entity.videoCard;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VideoCardRepository {
    private final EntityManager em;

    public void videoCardSave(videoCard vc)
    {
        em.persist(vc);
    }


    public List<videoCard> getPathVideoCardList(String childId, String userId){
        return em.createQuery("SELECT m FROM videoCard m WHERE m.childId =:childId and (m.userId =:userId or m.state =: 'Pathologist')", videoCard.class)
                .setParameter("childId", childId)
                .setParameter("userId",userId)
                .getResultList();
    }

    public List<videoCard> getNokVideoCardList(String childId){
        return em.createQuery("SELECT m FROM videoCard m WHERE m.childId =:childId ", videoCard.class)
                .setParameter("childId", childId)
                .getResultList();
    }
}
