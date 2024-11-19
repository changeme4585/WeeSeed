package org.weeseed.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.weeseed.entity.AacCard;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AacCardRepository {
    private final EntityManager em;
    private final UserInfoRepository userInfoRepository;

    /**
     * 데이터베이스에 하나의 AAC 카드 저장
     *
     * @param aacCard 저장할 AAC 카드 엔티티
     */
    public void saveAacCard(AacCard aacCard) {
        em.persist(aacCard);
    }

    /**
     * 재활사가 AAC 카드 목록을 조회하는 경우 수행
     * 현재 계정의 사용자 ID와 아동 ID로 AAC 카드 목록을 검색
     * 재활사는 보호자의 카드도 열람할 수 있음
     *
     * @param childId       카드가 생성된 계정의 아동 ID
     * @param constructorId 카드가 생성된 계정의 사용자 ID
     * @return              해당하는 AAC 카드 목록
     */
    public List<AacCard> getPathAacCardList(String childId, String constructorId) {
        return em.createQuery(
                        "SELECT m FROM AacCard m WHERE m.childId = :childId " +
                                "AND (m.constructorId = :constructorId OR m.state = 'Nok')", AacCard.class)
                .setParameter("childId", childId)
                .setParameter("constructorId", constructorId)
                .getResultList();
    }

    /**
     * 보호자가 AAC 카드 목록을 조회하는 경우 수행
     * 현재 계정의 아동 ID로 AAC 카드 목록을 검색
     * 보호자는 아동에게 등록된 모든 재활사의 카드를 열람할 수 있음
     *
     * @param childId 카드가 생성된 계정의 아동 ID
     * @return        해당하는 아동의 AAC 카드 목록
     */
    public List<AacCard> getNokAacCardList(String childId) {
        return em.createQuery(
                        "SELECT m FROM AacCard m WHERE m.childId = :childId", AacCard.class)
                .setParameter("childId", childId)
                .getResultList();
    }

    /**
     * 특정 생성자의 아동 계정에서 단일 AAC 카드 삭제
     *
     * @param aacCard 삭제할 카드 엔티티
     */
    public void deleteAacCard(AacCard aacCard) {
        em.remove(aacCard);
    }


    /**
     * 특정 생성자의 아동 계정에서 단일 AAC 카드 수정
     *
     * @param aacCard 수정 AAC 카드 객체
     */
    public void updateAacCard(AacCard aacCard) {
        // 없다면 새로운 엔티티가 생성됨
        // 필요하다면 exist 검사할 것
        em.merge(aacCard);
    }

    /**
     * 사용자 유형에 따라 AAC 카드 목록을 검색
     *
     * @param userId   사용자 ID
     * @param childId  아동 ID
     * @return         AAC 카드 목록
     */
    public List<AacCard> findByUserId(String userId, String childId) {
        String state = userInfoRepository.getUserState(userId); //사용자 유형 ('Path' 또는 'Nok')

        if ("Path".equalsIgnoreCase(state)) {
            return getPathAacCardList(childId, userId);
        } else if ("Nok".equalsIgnoreCase(state)) {
            return getNokAacCardList(childId);
        } else {
            throw new IllegalArgumentException("Unknown user type: " + state);
        }
    }

    /**
     * 특정 생성자가 특정 날짜에 만든 AAC 카드 목록을 검색
     * 오늘 생성한 AAC 카드 목록 조회에 사용
     *
     * @param creationTime  AAC 카드의 생성 시간
     * @param constructorId 카드가 생성된 계정의 사용자 ID
     * @return 특정 일시에 생성된 AAC 카드 목록
     */
    public List<AacCard> getTodayAacCardList(String creationTime, String constructorId) {
        return em.createQuery(
                        "SELECT m FROM AacCard m WHERE m.creationTime = :creationTime " +
                                "AND m.constructorId = :constructorId", AacCard.class)
                .setParameter("creationTime", creationTime)
                .setParameter("constructorId", constructorId)
                .getResultList();
    }

    /**
     * 카드의 ID로 AAC 카드를 검색
     * Renamed from {@code getAacCard} during refactoring.
     *
     * @param aacCardId AAC 카드 ID
     * @return AAC 카드 개체, 또는 찾지 못한 경우 null
     */
    public AacCard findAacCardById(Long aacCardId) {
        try {
            return em.createQuery(
                            "SELECT m FROM AacCard m WHERE m.aacCardId = :aacCardId", AacCard.class)
                    .setParameter("aacCardId", aacCardId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 데이터베이스의 모든 AAC 카드를 검색
     * Renamed from {@code getAllAacCard} during refactoring.
     *
     * @return 모든 AAC 카드 목록
     */
    public List<AacCard> findAllAacCards() {
        return em.createQuery("SELECT m FROM AacCard m", AacCard.class)
                .getResultList();
    }
}
