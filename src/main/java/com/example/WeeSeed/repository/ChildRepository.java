package com.example.WeeSeed.repository;


import com.example.WeeSeed.entity.Child;
import com.example.WeeSeed.entity.Nok;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChildRepository {

    private final EntityManager em;

    public int checkChildDuplicate(String childCode){ //중복된 아동 난수 코드가 존재하는지 확인하는 코드
        return em.createQuery("SELECT m FROM Child m WHERE m.childCode =:childCode", Child.class)
                .setParameter("childCode", childCode)
                .getResultList().size();
    }

    public Child getChild(String childCode){ //아동 코드로 아동을 불러오는 로직
        return em.createQuery("SELECT m FROM Child m WHERE m.childCode =:childCode", Child.class)
                .setParameter("childCode", childCode)
                .getResultList().get(0);
    }

    public List<Child> getChildByUser(String userId){   //사용자에 연결된 아동을 불러오는 코드
        return em.createQuery("SELECT m FROM Child m WHERE m.userId =:userId", Child.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public String getChildBirth(String childCode){
        String birth="";
    try {
        birth =  em.createQuery("SELECT m FROM Child m WHERE m.childCode =:childCode", Child.class)
                .setParameter("childCode", childCode)
                .getResultList().get(0).getBirth();
        }catch (Exception e){
            System.out.println("아동 출력 오류: "+childCode);
        }
        return birth;
    }
}
