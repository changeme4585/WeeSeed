package com.example.WeeSeed.repository;

import com.example.WeeSeed.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SignInRepository {
    private final EntityManager em ;

    public void registUser(User user){
        em.persist(user);
    }
}
