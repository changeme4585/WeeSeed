package org.weeseed.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
@RequiredArgsConstructor
public class FindUserInfoRepository {
    private final EntityManager em;

    /**
     * 사용자의 이름과 이메일을 기반으로 사용자 ID 찾기
     *
     * @param name  사용자 이름
     * @param email 사용자 이메일
     * @return 사용자 ID
     * @throws ResponseStatusException 사용자가 존재하지 않을 경우, 404 NOT FOUND 예외 발생
     */
    public String findUserId(String name, String email) {
        try {
            return em.createQuery("SELECT m.userId FROM User m WHERE m.name = :name AND m.email = :email", String.class)
                    .setParameter("name", name)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with the provided name and email.");
        }
    }
}
