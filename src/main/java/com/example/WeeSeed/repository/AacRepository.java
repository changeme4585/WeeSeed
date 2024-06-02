package com.example.WeeSeed.repository;

import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.entity.Child;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AacRepository {
    private final EntityManager em;
    public void aacCardSave(AacCard aacCard){
        em.persist(aacCard);
    }
    public List<AacCard> getPathAacCardList(String childId, String constructorId){

        return em.createQuery("SELECT m FROM AacCard m WHERE m.childId =:childId and (m.constructorId =:constructorId or m.state = 'Nok')", AacCard.class)
                .setParameter("childId", childId)
                .setParameter("constructorId",constructorId)
                .getResultList();
    }

    public List<AacCard> getNokAacCardList(String childId){
        return em.createQuery("SELECT m FROM AacCard m WHERE m.childId =:childId", AacCard.class)
                .setParameter("childId", childId)
                .getResultList();
    }
}
