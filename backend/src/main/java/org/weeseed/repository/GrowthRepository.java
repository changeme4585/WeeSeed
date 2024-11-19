package org.weeseed.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.weeseed.entity.DailyLearningLog;
import org.weeseed.entity.Growth;
import org.weeseed.entity.GrowthDiary;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GrowthRepository {
    private final EntityManager em;

    /**
     * 성장 기록을 데이터베이스에 저장
     * Renamed from {@code writeGrowth} during refactoring.
     *
     * @param growth 저장할 성장 엔티티
     */
    public void saveGrowth(Growth growth) {
        em.persist(growth);
    }

    /**
     * 성장일지를 데이터베이스에 저장
     * Renamed from {@code writeGrowthDiary} during refactoring.
     * @param growthDiary 저장할 성장일지 엔티티
     */
    public void saveGrowthDiary(GrowthDiary growthDiary) {
        em.persist(growthDiary);
    }

    /**
     * 특정 생성자가 특정 날짜에 만든 성장 기록을 검색
     * Renamed from {@code getGrowth} during refactoring.

     * @param creationTime 성장 기록 생성 시간
     * @param userId       성장 기록을 생성한 사용자 ID
     * @return             해당하는 성장 기록 목록
     */
    public List<Growth> findGrowthByTime(String creationTime, String userId) {
        return em.createQuery("SELECT m FROM Growth m WHERE m.creationTime = :creationTime AND m.userId = :userId", Growth.class)
                .setParameter("creationTime", creationTime)
                .setParameter("userId", userId)
                .getResultList(); // 결과 리스트 반환
    }

    /**
     * 특정 사용자와 아동 코드로 성장일지를 조회
     * Renamed from {@code getGrowthDiary} during refactoring.
     *
     * @param userId    성장일지를 조회할 사용자 ID
     * @param childCode 조회할 아동 코드
     * @return          해당하는 성장일지 목록
     */
    public List<GrowthDiary> findGrowthDiaryByUserAndChild(String userId, String childCode) {
        return em.createQuery("SELECT m FROM GrowthDiary m WHERE m.userId = :userId AND m.childCode = :childCode", GrowthDiary.class)
                .setParameter("userId", userId)
                .setParameter("childCode", childCode)
                .getResultList(); // 결과 리스트 반환
    }

    /**
     * 일일 학습일지를 데이터베이스에 저장
     * Renamed from {@code writeLearning} during refactoring.
     *
     * @param dailyLearningLog 저장할 학습일지 엔티티
     */
    public void saveLearningLog(DailyLearningLog dailyLearningLog) {
        em.persist(dailyLearningLog); // 학습일지 객체를 영속화
    }

    /**
     * 모든 학습일지를 조회
     * Renamed from {@code getAllLearning} during refactoring.
     *
     * @return 모든 학습일지 목록
     */
    public List<DailyLearningLog> findAllLearningLogs() {
        return em.createQuery("SELECT m FROM DailyLearningLog m", DailyLearningLog.class).getResultList(); // 결과 리스트 반환
    }

    /**
     * 첫 번째 학습일지의 날짜를 비교하여 반환
     * Renamed from {@code compareLearningDate} during refactoring.
     *
     * @return 첫 번째 학습일지의 날짜
     */
    public String getFirstLearningLogDate() {
        return em.createQuery("SELECT m FROM DailyLearningLog m", DailyLearningLog.class)
                .getResultList()
                .get(0) // 첫 번째 결과 가져오기
                .getDate(); // 날짜 반환
    }

    /**
     * 특정 카드 ID와 카드 타입으로 학습일지를 조회
     * Renamed from {@code getLearningCard} during refactoring.
     *
     * @param cardId  카드 ID
     * @param cardType 카드 타입
     * @return 해당하는 학습일지 목록
     */
    public List<DailyLearningLog> findLearningLogsByCard(Long cardId, String cardType) {
        return em.createQuery("SELECT m FROM DailyLearningLog m WHERE m.cardId = :cardId AND m.cardType = :cardType", DailyLearningLog.class)
                .setParameter("cardId", cardId)
                .setParameter("cardType", cardType)
                .getResultList(); // 결과 리스트 반환
    }

    /**
     * 학습일지를 삭제
     * Renamed from {@code deleteLearning} during refactoring.
     *
     * @param dailyLearningLog 삭제할 학습일지 엔티티
     */
    public void deleteLearningLog(DailyLearningLog dailyLearningLog) {
        em.remove(dailyLearningLog); // 학습일지 객체 제거
    }

    /**
     * 특정 날짜, 아동 ID, 사용자 ID로 학습일지 조회
     * Renamed from {@code getLearning} during refactoring.
     *
     * @param date    학습일지의 날짜
     * @param childId 아동 ID
     * @param userId  사용자 ID
     * @return 해당하는 학습일지 목록
     */
    public List<DailyLearningLog> findLearningLogsByDateAndChild(String date, String childId, String userId) {
        return em.createQuery("SELECT m FROM DailyLearningLog m WHERE m.childId = :childId AND m.userId = :userId AND m.date = :date", DailyLearningLog.class)
                .setParameter("childId", childId)
                .setParameter("userId", userId)
                .setParameter("date", date)
                .getResultList(); // 결과 리스트 반환
    }
}
