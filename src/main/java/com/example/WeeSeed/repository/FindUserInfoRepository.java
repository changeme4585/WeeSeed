package com.example.WeeSeed.repository;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FindUserInfoRepository {
    private final EntityManager em;

    public  String findUserId(String name, String email){
        return em.createQuery("select m.userId from User m where m.name =:name and m.email =:email",String.class)
                .setParameter("name",name)
                .setParameter("email",email).getSingleResult();
    }
}
