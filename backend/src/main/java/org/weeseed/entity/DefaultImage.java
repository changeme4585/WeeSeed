package org.weeseed.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "default_image")
@Getter
@NoArgsConstructor
public class DefaultImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "constructor_id", nullable = false)
    private String constructorId;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Builder
    public DefaultImage(String constructorId, String cardName) {
        this.constructorId = constructorId;
        this.cardName = cardName;
    }

    public void updateCardName(String cardName) {
        this.cardName = cardName;
    }
}
