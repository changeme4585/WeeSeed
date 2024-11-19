package org.weeseed.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.weeseed.entity.Child;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChildRepository {
    private final EntityManager em;

    /**
     * 주어진 아동 코드가 데이터베이스에 존재하는지 확인
     *
     * @param childCode 중복을 확인할 아동 코드
     * @return 중복된 아동 코드의 수
     */
    public int checkChildDuplicate(String childCode) {
        List<Child> results = em.createQuery(
                        "SELECT c FROM Child c WHERE c.childCode = :childCode", Child.class)
                .setParameter("childCode", childCode)
                .getResultList();
        return results.size();
    }

    /**
     * 아동 코드로 아동 정보를 조회
     *
     * @param childCode 조회할 아동 코드
     * @return 해당 아동 객체
     * @throws NoResultException 아동 코드로 조회된 결과가 없을 경우
     */
    public Child getChild(String childCode) {
        try {
            return em.createQuery(
                            "SELECT c FROM Child c WHERE c.childCode = :childCode", Child.class)
                    .setParameter("childCode", childCode)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultException("No child found with childCode: " + childCode);
        }
    }

    public List<Child> getChilds(String childCode) {
        return em.createQuery(
                        "SELECT c FROM Child c WHERE c.childCode = :childCode", Child.class)
                .setParameter("childCode", childCode)
                .getResultList();
    }

    /**
     * 사용자 ID로 등록된 아동 목록을 조회
     *
     * @param userId 사용자 ID
     * @return 해당 사용자에 연결된 아동 목록
     */
    public List<Child> getChildByUser(String userId) {
        return em.createQuery(
                        "SELECT c FROM Child c WHERE c.userId = :userId", Child.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * 주어진 아동 코드로 아동의 생년월일을 조회
     *
     * @param childCode 조회할 아동 코드
     * @return 해당 아동의 생년월일
     * @throws NoResultException 아동 코드로 조회된 결과가 없을 경우
     */
    public String getChildBirth(String childCode) {
        try {
            return em.createQuery(
                            "SELECT c.birth FROM Child c WHERE c.childCode = :childCode", String.class)
                    .setParameter("childCode", childCode)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultException("No birth date found for childCode: " + childCode);
        } catch (Exception e) {
            System.out.println("아동 출력 오류: " + childCode);
            return ""; // 생년월일을 찾지 못했을 경우 빈 문자열 반환
        }
    }
}
