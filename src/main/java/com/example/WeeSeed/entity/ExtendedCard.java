package com.example.WeeSeed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExtendedCard {
    @Id
    @Column
    private String representativeCardId;

    @Column
    private String imageUrl;

    @Column
    private int sequence;

    @Builder
    public  ExtendedCard(String representativeCardId,String imageUrl,int sequence){
        this.representativeCardId = representativeCardId;
        this.imageUrl = imageUrl;
        this.sequence = sequence;
    }

}
