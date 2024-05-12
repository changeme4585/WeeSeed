package com.example.WeeSeed.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hireID;

    @Column
    private String nId;

    @Column
    private String pID;

    @Builder
    public Hire(String nId,String pID){
        this.nId = nId;
        this.pID = pID;
    }
}
