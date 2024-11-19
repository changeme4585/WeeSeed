package org.weeseed.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "growth")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Growth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "growth_id")
    private Long growthId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "color")
    private String color;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "creation_time")
    private String creationTime;

    public Growth(String imageUrl, String userId, String color, String cardName, String creationTime) {
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.color = color;
        this.cardName = cardName;
        this.creationTime = creationTime;
    }
}
