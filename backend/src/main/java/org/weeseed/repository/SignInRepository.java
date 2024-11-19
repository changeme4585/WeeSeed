package org.weeseed.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.weeseed.entity.*;

@Repository
@RequiredArgsConstructor
public class SignInRepository {
    private final EntityManager em;

    // 사용자 등록 메소드
    public void registerUser(User user) {
        em.persist(user);
    }

    // 재활사 등록 메소드
    public void registerPathologist(Pathologist pathologist) {
        em.persist(pathologist);
    }

    // 보호자 등록 메소드
    public void registerNok(Nok nok) {
        em.persist(nok);
    }

    // 아동 등록 메소드
    public void registerChild(Child child) {
        em.persist(child);
    }

    // 기본 이미지 저장 메소드
    public void saveDefaultImage(DefaultImage defaultImage) {
        em.persist(defaultImage);
    }
}
