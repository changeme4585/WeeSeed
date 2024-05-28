package com.example.WeeSeed.repository;


import com.example.WeeSeed.entity.Child;
import com.example.WeeSeed.entity.Nok;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChildRepository {

    private final EntityManager em;

    public int checkChildDuplicate(String childCode){ //중복된 아동 난수 코드가 존재하는지 확인하는 코드
        return em.createQuery("SELECT m FROM Child m WHERE m.childCode =:childCode", Child.class)
                .setParameter("childCode", childCode)
                .getResultList().size();
    }
}
