package org.weeseed.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.weeseed.entity.VideoCard;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VideoCardRepository {
    private final EntityManager em;

    /**
     * 데이터베이스에 하나의 비디오 카드 저장
     *
     * @param videoCard 저장할 비디오 카드 엔티티
     */
    public void saveVideoCard(VideoCard videoCard) {
        em.persist(videoCard);
    }

    /**
     * 재활사가 비디오 카드 목록을 조회하는 경우 수행
     * 현재 계정의 사용자 ID와 아동 ID로 AAC 카드 목록을 검색
     * 재활사는 보호자의 카드도 열람할 수 있음
     *
     * @param childId   카드가 생성된 계정의 아동 ID
     * @param userId    카드가 생성된 계정의 사용자 ID
     * @return          해당하는 AAC 카드 목록
     */
    public List<VideoCard> getPathVideoCardList(String childId, String userId) {
        return em.createQuery("SELECT m FROM VideoCard m WHERE m.childId = :childId AND (m.userId = :userId OR m.state = 'Nok')", VideoCard.class)
                .setParameter("childId", childId)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * 보호자가 AAC 카드 목록을 조회하는 경우 수행
     * 현재 계정의 아동 ID로 AAC 카드 목록을 검색
     * 보호자는 아동에게 등록된 모든 재활사의 카드를 열람할 수 있음
     *
     * @param childId 카드가 생성된 계정의 아동 ID
     * @return        해당하는 아동의 비디오 카드 목록
     */
    public List<VideoCard> getNokVideoCardList(String childId) {
        return em.createQuery("SELECT m FROM VideoCard m WHERE m.childId = :childId", VideoCard.class)
                .setParameter("childId", childId)
                .getResultList();
    }

    /**
     * 특정 생성자가 특정 날짜에 만든 비디오 카드 목록을 검색
     * 오늘 생성한 비디오 카드 목록 조회에 사용
     *
     * @param creationTime 비디오 카드의 생성 시간
     * @param userId       카드가 생성된 계정의 사용자 ID
     * @return             특정 일시에 생성된 AAC 카드 목록
     */
    public List<VideoCard> getTodayVideoCardList(String creationTime, String userId) {
        return em.createQuery("SELECT m FROM VideoCard m WHERE m.creationTime = :creationTime AND m.userId = :userId", VideoCard.class)
                .setParameter("creationTime", creationTime)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * 카드의 ID로 비디오 카드를 검색
     * Renamed from {@code getVideoCard} during refactoring.
     *     *
     * @param videoCardId 비디오 카드 ID
     * @return 비디오 카드 엔티티, 또는 찾지 못한 경우 null
     */
    public VideoCard findVideoCardById(Long videoCardId) {
        try {
            return em.createQuery("SELECT m FROM VideoCard m WHERE m.videoCardId = :videoCardID", VideoCard.class)
                    .setParameter("videoCardID", videoCardId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 데이터베이스의 모든 비디오 카드를 검색
     * Renamed from {@code getAllVideoCard} during refactoring.
     *
     * @return 모든 비디오 카드 목록
     */
    public List<VideoCard> findAllVideoCards() {
        return em.createQuery("SELECT m FROM VideoCard m", VideoCard.class)
                .getResultList();
    }

    /**
     * 카드 이름으로 비디오 카드를 찾습니다.
     * Renamed from {@code findByName} during refactoring.
     *
     * @param childId 자녀 ID
     * @param userId 사용자 ID
     * @param cardName 카드 이름
     * @return 비디오 카드 엔티티
     */
    public VideoCard findVideoCardByName(String childId, String userId, String cardName) {
        return em.createQuery("SELECT m FROM VideoCard m WHERE m.childId = :childId AND m.userId = :userId AND m.cardName = :cardName", VideoCard.class)
                .setParameter("childId", childId)
                .setParameter("userId", userId)
                .setParameter("cardName", cardName)
                .getSingleResult(); // 단일 결과 반환
    }

    /**
     * 비디오 카드를 삭제합니다.
     *
     * @param videoCard 삭제할 비디오 카드 엔티티
     */
    public void deleteVideoCard(VideoCard videoCard) {
        em.remove(videoCard);
    }
}
