package org.weeseed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weeseed.dto.Statistic.AgeDto;
import org.weeseed.dto.Statistic.GenderDto;
import org.weeseed.dto.Statistic.GradeDto;
import org.weeseed.dto.Statistic.TypeDto;
import org.weeseed.dto.StatisticDto;
import org.weeseed.entity.AacCard;
import org.weeseed.entity.VideoCard;
import org.weeseed.repository.AacCardRepository;
import org.weeseed.repository.ChildRepository;
import org.weeseed.repository.StatisticRepository;
import org.weeseed.repository.VideoCardRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticRepository statisticRepository;
    private final AacCardRepository aacCardRepository;
    private final VideoCardRepository videoCardRepository;
    private final ChildRepository childRepository;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.s3.folder.images}")
    private String imageFolder;

    @Value("${cloud.aws.s3.folder.videos}")
    private String videoFolder;

    @Value("${cloud.aws.s3.folder.thumbs}")
    private String thumbFolder;

    // 생일 문자열을 받아 나이를 계산하는 메서드
    public int calculateAge(String birthdayStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
        LocalDate birthday = LocalDate.parse(birthdayStr, formatter);
        return Period.between(birthday, LocalDate.now()).getYears(); // 현재 나이 계산
    }

    // AAC 카드와 비디오 카드의 나이 통계 수집 메서드
    public AgeDto ageStatistic() {
        List<AacCard> aacCards = aacCardRepository.findAllAacCards();
        List<VideoCard> videoCards = videoCardRepository.findAllVideoCards();
        // 나이별 카운트 초기화
        int[] ageCountAac = new int[4]; // 0-5, 5-10, 10-15, 15 이상
        int[] ageCountVideo = new int[4]; // 비디오 카드 카운트

        // AAC 카드 나이 계산
        for (AacCard aacCard : aacCards) {
            int childAge = calculateAge(childRepository.getChildBirth(aacCard.getChildId()));
            if (childAge < 5) ageCountAac[0]++;
            else if (childAge < 10) ageCountAac[1]++;
            else if (childAge < 15) ageCountAac[2]++;
            else ageCountAac[3]++;
        }

        // 비디오 카드 나이 계산
        for (VideoCard videoCard : videoCards) {
            int childAge = calculateAge(childRepository.getChildBirth(videoCard.getChildId()));
            if (childAge < 5) ageCountVideo[0]++;
            else if (childAge < 10) ageCountVideo[1]++;
            else if (childAge < 15) ageCountVideo[2]++;
            else ageCountVideo[3]++;
        }

        // AgeDto 객체 생성 및 반환
        return AgeDto.builder()
                .zeroAac(ageCountAac[0]).fiveAac(ageCountAac[1])
                .tenAac(ageCountAac[2]).fifteenAac(ageCountAac[3])
                .zeroVideo(ageCountVideo[0]).fiveVideo(ageCountVideo[1])
                .tenVideo(ageCountVideo[2]).fifteenVideo(ageCountVideo[3])
                .build();
    }

    // 성별 통계 수집 메서드
    public GenderDto genderStatistic() {
        GenderDto genderDto = GenderDto.builder()
                .maleAac(statisticRepository.getAacCardByGender("M").size())
                .femaleAac(statisticRepository.getAacCardByGender("F").size())
                .maleVideo(statisticRepository.getVideoCardByGender("M").size())
                .femaleVideo(statisticRepository.getVideoCardByGender("F").size())
                .build();
        return genderDto;
    }

    // 장애 유형 통계 수집 메서드
    public TypeDto disabilityType() {
        TypeDto typeDto = TypeDto.builder()
                .autism(Math.toIntExact(statisticRepository.getTypeNum("자폐성장애")))
                .intellectual(Math.toIntExact(statisticRepository.getTypeNum("지적장애")))
                .behavioral(Math.toIntExact(statisticRepository.getTypeNum("행동장애")))
                .pronunciation(Math.toIntExact(statisticRepository.getTypeNum("발음장애")))
                .brainLesion(Math.toIntExact(statisticRepository.getTypeNum("뇌병변장애")))
                .build();
        return typeDto;
    }

    // 장애 학년 통계 수집 메서드
    public GradeDto disabilityGrade() {
        GradeDto gradeDto = GradeDto.builder()
                .oneAac(Math.toIntExact(statisticRepository.getGradeNum("1")))
                .twoAac(Math.toIntExact(statisticRepository.getGradeNum("2")))
                .threeAac(Math.toIntExact(statisticRepository.getGradeNum("3")))
                .fourAac(Math.toIntExact(statisticRepository.getGradeNum("4")))
                .fiveAac(Math.toIntExact(statisticRepository.getGradeNum("5")))
                .sixAac(Math.toIntExact(statisticRepository.getGradeNum("6")))
                .build();
        return gradeDto;
    }

    public List<StatisticDto> getPersonalStatistic(String childId,String userId){
        List<StatisticDto> statisticDtoList = new ArrayList<>();
        statisticDtoList.add(getDateStatistic(1,childId,userId));
        statisticDtoList.add(getDateStatistic(7,childId,userId));
        statisticDtoList.add(getDateStatistic(30,childId,userId));
        return statisticDtoList;
    }

    // 특정 기간에 대한 통계 수집 메서드
    public StatisticDto getDateStatistic(int num,String childId,String userId){
        System.out.println(num+"일 통계");
        List<AacCard> aacCardList =statisticRepository.getAacCard(childId,userId);
        List<VideoCard> videoCardList = statisticRepository.getVideoCard(childId,userId);

        int aacIdx = 0 ;
        int videoIdx = 0 ;

        String cardName = "";
        String image = "";
        String color = "";
        int maxCnt  = 0 ;

        LocalDate today = LocalDate.now();
        LocalDate rangeStart = today.minusDays(num - 1); // num일 전부터 오늘까지

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");

        int imageCardNum = 0;
        int videoCardNum = 0;

        // AAC 카드 처리
        while (aacIdx < aacCardList.size()) {
            AacCard card = aacCardList.get(aacIdx);
            LocalDate creationDate = LocalDate.parse(card.getCreationTime(), formatter);

            // 날짜 범위 필터링
            if (creationDate.isBefore(rangeStart) || creationDate.isAfter(today)) {
                aacIdx++;
                continue;
            }

            imageCardNum++;

            System.out.println("AAC 카드 개수: " + card.getClickCnt());
            if (maxCnt < card.getClickCnt()) {
                cardName = card.getCardName();
                image = imageFolder + card.getImageUrl();
                color = card.getColor();
                maxCnt = card.getClickCnt();
            }

            aacIdx++;
        }

        // 비디오 카드 처리
        while (videoIdx < videoCardList.size()) {
            VideoCard card = videoCardList.get(videoIdx);
            LocalDate creationDate = LocalDate.parse(card.getCreationTime(), formatter);

            // 날짜 범위 필터링
            if (creationDate.isBefore(rangeStart) || creationDate.isAfter(today)) {
                videoIdx++;
                continue;
            }

            videoCardNum++;
            System.out.println("비디오 카드 개수: " + card.getClickCnt());
            if (maxCnt < card.getClickCnt()) {
                cardName = card.getCardName();
                image = thumbFolder + card.getThumbnailUrl();
                color = card.getColor();
                maxCnt = card.getClickCnt();
            }

            videoIdx++;
        }


        StatisticDto statisticDto = StatisticDto.builder().
                imageCardNum(imageCardNum).
                videoCardNum(videoCardNum).
                cardName(cardName).
                image(image).
                color(color).
                build();

        return statisticDto;
    }
}
