package com.example.WeeSeed.repository;

import com.example.WeeSeed.entity.DefaultImage;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DefaultImageRepository {
    private final EntityManager em;

    public List<DefaultImage> getUserDefaultImageList(String constructorId) {
       return  em.createQuery("select m from DefaultImage m where m.constructorId =:constructorId",DefaultImage.class).
                setParameter("constructorId",constructorId).
                getResultList();
    }
    public  void deleteDefaultImage(DefaultImage defaultImage){
        em.remove(defaultImage);
    }

    public DefaultImage findByCardName(String constructorId,String cardName) {
        return  em.createQuery("select m from DefaultImage m where m.constructorId =:constructorId and m.cardName =:cardName",DefaultImage.class).
                setParameter("constructorId",constructorId).
                setParameter("cardName",cardName).
                getSingleResult();
    }
}
