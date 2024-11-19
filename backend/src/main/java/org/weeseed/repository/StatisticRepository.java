package org.weeseed.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.weeseed.entity.AacCard;
import org.weeseed.entity.VideoCard;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatisticRepository {
    private final EntityManager em;

    /**
     * 주어진 성별에 해당하는 AAC 카드 목록 조회
     * Renamed from {@code getAaCardGender} during refactoring.
     *
     * @param gender 성별 필터 기준
     * @return 해당 성별의 AAC 카드 목록
     */
    public List<AacCard> getAacCardByGender(String gender) {
        return em.createQuery("SELECT DISTINCT a FROM AacCard a " +
                        "JOIN Child c ON a.childId = c.childCode WHERE c.gender = :gender", AacCard.class)
                .setParameter("gender", gender)
                .getResultList();
    }

    /**
     * 주어진 성별에 해당하는 비디오 카드 목록을 조회
     * Renamed from {@code getVideoCardGender} during refactoring.
     *
     * @param gender 성별 필터 기준
     * @return 해당 성별의 비디오 카드 목록
     */
    public List<VideoCard> getVideoCardByGender(String gender) {
        return em.createQuery("SELECT DISTINCT v FROM VideoCard v " +
                        "JOIN Child c ON v.childId = c.childCode WHERE c.gender = :gender", VideoCard.class)
                .setParameter("gender", gender)
                .getResultList();
    }

    /**
     * 특정 아동과 생성자 ID에 해당하는 AAC 카드 목록을 조회
     *
     * @param childId 아동 ID
     * @param constructorId 생성자 ID
     * @return 해당 아동 및 생성자에 대한 AAC 카드 목록
     */
    public List<AacCard> getAacCard(String childId, String constructorId) {
        return em.createQuery("SELECT m FROM AacCard m " +
                        "WHERE m.childId = :childId AND m.constructorId = :constructorId " +
                        "ORDER BY m.creationTime DESC", AacCard.class)
                .setParameter("childId", childId)
                .setParameter("constructorId", constructorId)
                .getResultList();
    }

    /**
     * 특정 아동과 사용자 ID에 해당하는 비디오 카드 목록을 조회
     *
     * @param childId 아동 ID
     * @param userId 사용자 ID
     * @return 해당 아동 및 사용자에 대한 비디오 카드 목록
     */
    public List<VideoCard> getVideoCard(String childId, String userId) {
        return em.createQuery("SELECT m FROM VideoCard m " +
                        "WHERE m.childId = :childId AND m.userId = :userId " +
                        "ORDER BY m.creationTime DESC", VideoCard.class)
                .setParameter("childId", childId)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * 특정 등급의 아동 수를 조회
     *
     * @param grade 등급
     * @return 해당 등급의 아동 수
     */
     public Long getGradeNum(String grade) {
         return em.createQuery("SELECT COUNT(DISTINCT c.childCode) FROM Child c " +
                 "WHERE c.grade = :grade", Long.class)
                 .setParameter("grade", grade)
                 .getSingleResult();
     }

    /**
     * 특정 장애 유형의 아동 수를 조회
     *
     * @param disabilityType 장애 유형
     * @return 해당 장애 유형의 아동 수
     */
    public Long getTypeNum(String disabilityType) {
        return em.createQuery("SELECT COUNT(DISTINCT c.childCode) FROM Child c " +
                        "WHERE c.disabilityType = :disabilityType", Long.class)
                .setParameter("disabilityType", disabilityType)
                .getSingleResult();
    }


}
