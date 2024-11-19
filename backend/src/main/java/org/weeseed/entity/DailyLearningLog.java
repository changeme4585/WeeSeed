package org.weeseed.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="daily_learning_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DailyLearningLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long learnId;

    @Column
    private Long cardId;

    @Column
    private String cardType;

    @Column
    private String childId;

    @Column
    private String date;

    @Column
    private int clickCnt;

    @Column
    private String imageUrl;

    @Column
    private String color;

    @Column
    private String cardName;

    @Column
    private String userId;

    public void updateClick() {
        this.clickCnt += 1;
    }

    @Builder
    public DailyLearningLog(Long cardId, String cardType, String childId, String date,
                            int clickCnt, String imageUrl, String color, String cardName, String userId) {
        this.cardId = cardId;
        this.cardType = cardType;
        this.childId = childId;
        this.date = date;
        this.clickCnt = clickCnt;
        this.imageUrl = imageUrl;
        this.color = color;
        this.cardName = cardName;
        this.userId = userId;
    }
}
