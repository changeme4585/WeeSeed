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


    public List<videoCard> getVideoCardList(String childId, String userId){
        return em.createQuery("SELECT m FROM videoCard m WHERE m.childId =:childId and m.userId =:userId ", videoCard.class)
                .setParameter("childId", childId)
                .setParameter("userId",userId)
                .getResultList();
    }
}
