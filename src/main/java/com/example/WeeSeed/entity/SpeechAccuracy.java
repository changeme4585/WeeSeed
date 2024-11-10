package com.example.WeeSeed.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SpeechAccuracy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long speechId;

    @Column(name = "child_id")
    private String childId;

    @Column(name = "card_name")
    private  String cardName;

    @Column(name = "score")
    private  String score;

   
}
