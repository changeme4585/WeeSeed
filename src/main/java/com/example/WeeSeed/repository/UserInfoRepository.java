package com.example.WeeSeed.repository;

import com.example.WeeSeed.entity.Nok;
import com.example.WeeSeed.entity.Pathologist;
import com.example.WeeSeed.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserInfoRepository {
    private final EntityManager em;

    public List<User> checkUserLogIn(String userId,String password)
    {
        return em.createQuery("SELECT m FROM User m WHERE m.userId =:userId and  m.password =: password", User.class)
                .setParameter("userId", userId)
                .setParameter("password",password).getResultList();
    }

    public Nok getNokInfo(String nokId){
        try {
            return em.createQuery("SELECT m FROM Nok m WHERE m.nokId =:nokId", Nok.class)
                    .setParameter("nokId", nokId)
                    .getResultList().get(0);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nok not found");
        }
    }

    public Pathologist getPathInfo(String pathId) {
        //try {
            return em.createQuery("SELECT m FROM Pathologist m WHERE m.pathologistId =:pathId", Pathologist.class)
                    .setParameter("pathId", pathId)
                    .getResultList().get(0);
//        } catch (Exception e) {
//
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nok not found");
//        }
    }
}
