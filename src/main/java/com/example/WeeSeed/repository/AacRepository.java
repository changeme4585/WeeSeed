package com.example.WeeSeed.repository;

import com.example.WeeSeed.entity.AacCard;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AacRepository {
    private final EntityManager em;
    public void aacCardSave(AacCard aacCard){
        em.persist(aacCard);
    }
}
