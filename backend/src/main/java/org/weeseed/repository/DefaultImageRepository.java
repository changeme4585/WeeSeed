package org.weeseed.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.weeseed.entity.DefaultImage;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DefaultImageRepository {

    private final EntityManager em;

    /**
     * 사용자의 기본 이미지 목록을 조회
     *
     * Renamed from {@code findDefaultImagesByUserId} during refactoring.
     *
     * @param constructorId 사용자의 ID
     * @return 해당 사용자의 기본 이미지 목록
     */
    public List<DefaultImage> findDefaultImagesByUserId(String constructorId) {
        return em.createQuery(
                        "SELECT m FROM DefaultImage m WHERE m.constructorId = :constructorId", DefaultImage.class)
                .setParameter("constructorId", constructorId)
                .getResultList();
    }

    /**
     * 기본 이미지를 삭제
     *
     * @param defaultImage 삭제할 기본 이미지 엔티티
     */
    public void deleteDefaultImage(DefaultImage defaultImage) {
        em.remove(defaultImage);
    }

    /**
     * 카드 이름으로 기본 이미지를 찾기.
     *
     * Renamed from {@code findDefaultImageByCardName} during refactoring.
     *
     * @param constructorId 사용자 ID
     * @param cardName      카드 이름
     * @return 해당하는 기본 이미지 엔티티
     */
    public DefaultImage findDefaultImageByCardName(String constructorId, String cardName) {
        return em.createQuery(
                        "SELECT m FROM DefaultImage m WHERE m.constructorId = :constructorId AND m.cardName = :cardName", DefaultImage.class)
                .setParameter("constructorId", constructorId)
                .setParameter("cardName", cardName)
                .getSingleResult();
    }
}
