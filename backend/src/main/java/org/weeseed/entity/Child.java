package org.weeseed.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Child")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_id")
    private Long childId;

    @Column(name = "child_code", nullable = false, unique = true)
    private String childCode;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "disability_type")
    private String disabilityType;

    @Column(name = "grade")
    private int grade;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth")
    private String birth;

    @Column(name = "name")
    private String name;


    @Builder
    public Child(String childCode, String userId, String disabilityType, int grade, String gender, String birth, String name) {
        this.childCode = childCode;
        this.userId = userId;
        this.disabilityType = disabilityType;
        this.grade = grade;
        this.gender = gender;
        this.birth = birth;
        this.name = name;
    }
}