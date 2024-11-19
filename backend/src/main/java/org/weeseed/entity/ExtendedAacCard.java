package org.weeseed.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "extended_aac_card")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ExtendedAacCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "extended_card_id")
    private Long extendedCardId;

    @Column(name = "rep_card_id", nullable = false)
    private Long repCardId;  // 대표카드 ID

    @Column(name = "image_url", nullable = false)
    private String imageUrl;  // 이미지 URL

    @Column(name = "order_num", nullable = false)
    private int order_num;  // 순서

    @Builder
    public ExtendedAacCard(Long extendedCardId, Long repCardId, String imageUrl, int order) {
        this.extendedCardId = extendedCardId;
        this.repCardId = repCardId;
        this.imageUrl = imageUrl;
        this.order_num = order;
    }
}