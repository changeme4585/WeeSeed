package com.example.WeeSeed.repository;

import com.example.WeeSeed.entity.Nok;
import com.example.WeeSeed.entity.Pathologist;
import com.example.WeeSeed.entity.User;
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
}
