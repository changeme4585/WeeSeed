package com.example.WeeSeed.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor
public class DefaultImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;


    @Column
    private String constructorId;

    @Column
    private String cardName;


    @Builder
    public  DefaultImage(String constructorId,String cardName){
        this.constructorId = constructorId;
        this.cardName = cardName;
    }
}
