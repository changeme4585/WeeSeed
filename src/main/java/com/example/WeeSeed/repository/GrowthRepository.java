package com.example.WeeSeed.repository;


import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.entity.Growth;
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
    public List<Growth> getGrowth(String creationTime,String userId){
        return em.createQuery("SELECT m FROM Growth m WHERE m.creationTime =:creationTime and m.userId =:userId ", Growth.class)
                .setParameter("creationTime", creationTime)
                .setParameter("userId",userId)
                .getResultList();
    }
}
