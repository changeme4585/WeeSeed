package org.weeseed.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import org.weeseed.entity.Nok;
import org.weeseed.entity.Pathologist;
import org.weeseed.entity.User;

import java.util.List;

/**
 * 사용자 정보를 관리하는 저장소
 *
 * 사용자, NOK, Pathologist 정보를 데이터베이스에서 조회
 */

@Repository
@RequiredArgsConstructor
public class UserInfoRepository {
    private final EntityManager em;

    /**
     * 사용자 로그인 정보를 확인
     *
     * @param userId 사용자 ID
     * @param password 사용자 비밀번호
     * @return 사용자 정보를 담은 리스트
     */
    public List<User> checkUserLogin(String userId, String password) {
        return em.createQuery("SELECT u FROM User u WHERE u.userId = :userId AND u.password = :password", User.class)
                .setParameter("userId", userId)
                .setParameter("password", password)
                .getResultList();
    }

    /**
     * 사용자 정보 조회
     *
     * @param userId 사용자 ID
     * @return 사용자 정보를 담은 리스트
     */
    public List<User> getUser(String userId) {
        return em.createQuery("SELECT m FROM User m WHERE m.userId = :userId", User.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * 특정 사용자의 카테고리 조회
     * 재활사Pathologist 또는 보호자NOK
     *
     * @param userId 사용자 ID
     * @return 사용자 종류
     * @throws ResponseStatusException 사용자 ID가 존재하지 않을 경우, 404 NOT FOUND 예외 발생
     */
    public String getUserState(String userId) {
        return em.createQuery("SELECT u FROM User u WHERE u.userId = :userId", User.class)
                .setParameter("userId", userId)
                .getResultList()
                .stream()
                .findFirst()
                .map(User::getState)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    /**
     * NOK 정보 조회
     *
     * @param nokId NOK ID
     * @return NOK 정보
     * @throws ResponseStatusException NOK ID가 존재하지 않을 경우, 404 NOT FOUND 예외 발생
     */
    public Nok getNokInfo(String nokId) {
        try {
            return em.createQuery("SELECT n FROM Nok n WHERE n.nokId = :nokId", Nok.class)
                    .setParameter("nokId", nokId)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nok not found"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while retrieving NOK information", e);
        }
    }

    /**
     * Pathologist 정보 조회
     *
     * @param pathologistId Pathologist ID
     * @return Pathologist 정보
     * @throws ResponseStatusException Pathologist ID가 존재하지 않을 경우, 404 NOT FOUND 예외 발생
     */
    public Pathologist getPathologistInfo(String pathologistId) {
        try {
            return em.createQuery("SELECT p FROM Pathologist p WHERE p.pathologistId = :pathologistId", Pathologist.class)
                    .setParameter("pathologistId", pathologistId)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pathologist not found"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while retrieving Pathologist information", e);
        }
    }

    /**
     * 사용자 정보를 삭제하는 메서드
     *
     * @param user 삭제할 사용자 객체
     */
    public void removeUser(User user) {
        em.remove(user); // 사용자 삭제
    }

    /**
     * NOK 정보를 삭제하는 메서드
     *
     * @param nok 삭제할 NOK 객체
     */
    public void removeNok(Nok nok) {
        em.remove(nok); // NOK 삭제
    }

    /**
     * Pathologist 정보를 삭제하는 메서드
     *
     * @param pathologist 삭제할 Pathologist 객체
     */
    public void removePathologist(Pathologist pathologist) {
        em.remove(pathologist); // Pathologist 삭제
    }
}
