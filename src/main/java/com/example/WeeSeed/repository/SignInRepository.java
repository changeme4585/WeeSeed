package com.example.WeeSeed.repository;

import com.example.WeeSeed.entity.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SignInRepository {
    private final EntityManager em ;

    public void registUser(User user){  //object로 가능?
        em.persist(user);
    }
    public void registPath(Pathologist path){
        em.persist(path);
    }
    public void registNok(Nok nok){
        em.persist(nok);
    }

    public void registChild(Child child) {em.persist(child);}

    //기본 이미지 저장하는 함수
    public  void saveDefaultImage(DefaultImage defaultImage){em.persist(defaultImage);}
}
