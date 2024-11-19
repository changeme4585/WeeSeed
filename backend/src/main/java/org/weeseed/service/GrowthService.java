package org.weeseed.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weeseed.dto.DailyLearningLogDto;
import org.weeseed.dto.GrowthDiaryDto;
import org.weeseed.dto.GrowthDto;
import org.weeseed.entity.*;
import org.weeseed.repository.AacCardRepository;
import org.weeseed.repository.GrowthRepository;
import org.weeseed.repository.UserInfoRepository;
import org.weeseed.repository.VideoCardRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GrowthService {

    private final AacCardRepository aacCardRepository;
    private final VideoCardRepository videoCardRepository;
    private final GrowthRepository growthRepository;
    private final UserInfoRepository userInfoRepository;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.s3.folder.images}")
    private String imageFolder;

    @Value("${cloud.aws.s3.folder.videos}")
    private String videoFolder;

    @Value("${cloud.aws.s3.folder.thumbs}")
    private String thumbFolder;

    // Logger 인스턴스 생성
    private static final Logger logger = LoggerFactory.getLogger(GrowthService.class);

    // S3 파일 URL을 생성하는 메소드
    private String getS3FileUrl(String fileName, String folderName) {
//        return "https://" + bucketName + ".s3.amazonaws.com/" + folderName + fileName;
        return fileName;
    }

    /**
     * 카드 클릭 로그를 기록하고 일일 학습 로그를 업데이트하는 메소드
     *
     * @param cardId 카드 ID
     * @param cardType 카드 유형
     */
    public void clickLog(Long cardId, String cardType) {
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = formatDate(now);

        String childId, userId, color, imageUrl, cardName;

        if ("aac".equals(cardType)) {
            AacCard aacCard = aacCardRepository.findAacCardById(cardId);
            childId = aacCard.getChildId();
            userId = aacCard.getConstructorId();
            imageUrl = getS3FileUrl(aacCard.getImageUrl(), imageFolder);
            color = aacCard.getColor();
            cardName = aacCard.getCardName();
            aacCard.updateClick(); // 클릭 수 업데이트
        } else {
            VideoCard videoCard = videoCardRepository.findVideoCardById(cardId);
            childId = videoCard.getChildId();
            userId = videoCard.getUserId();
            imageUrl = getS3FileUrl(videoCard.getThumbnailUrl(), thumbFolder);
            color = videoCard.getColor();
            cardName = videoCard.getCardName();
            videoCard.updateClick(); // 클릭 수 업데이트
        }

        // 이미지 URL 로그 출력
        logger.info("Card Clicked - User ID: {}, Card Name: {}, Image URL: {}", userId, cardName, imageUrl);

        logGrowth(userId, cardName, color, imageUrl, formattedDate); // 성장 로그 기록
        updateDailyLearningLog(cardId, cardType, formattedDate, childId, imageUrl, color, userId, cardName); // 일일 학습 로그 업데이트
    }

    /**
     * LocalDateTime을 특정 형식의 문자열로 변환하는 메소드
     *
     * @param dateTime 변환할 날짜 및 시간
     * @return 형식이 변환된 날짜 문자열
     */
    private String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
        return dateTime.format(formatter);
    }

    /**
     * 성장 로그를 기록하는 메소드
     *
     * @param userId 사용자 ID
     * @param cardName 카드 이름
     * @param color 카드 색상
     * @param imageUrl 카드 이미지 URL
     * @param formattedDate 생성 시간
     */
    private void logGrowth(String userId, String cardName, String color, String imageUrl, String formattedDate) {
        Growth growth = Growth.builder()
                .imageUrl(imageUrl)
                .userId(userId)
                .cardName(cardName)
                .color(color)
                .creationTime(formattedDate)
                .build();
        growthRepository.saveGrowth(growth);
    }

    /**
     * 오늘 학습한 카드를 업데이트하는 메소드
     *
     * @param cardId 카드 ID
     * @param cardType 카드 유형
     * @param date 날짜
     * @param childId 아동 ID
     * @param imageUrl 카드 이미지 URL
     * @param color 카드 색상
     * @param userId 사용자 ID
     * @param cardName 카드 이름
     */
    private void updateDailyLearningLog(Long cardId, String cardType, String date, String childId, String imageUrl, String color, String userId, String cardName) {
        List<DailyLearningLog> dailyLearningCards = growthRepository.findLearningLogsByCard(cardId, cardType);

        if (dailyLearningCards.isEmpty()) { // 학습 로그가 없으면 새로 생성
            DailyLearningLog dailyLearningLog = DailyLearningLog.builder()
                    .cardId(cardId)
                    .cardType(cardType)
                    .date(date)
                    .clickCnt(0) // 클릭 수 초기화
                    .childId(childId)
                    .imageUrl(imageUrl)
                    .color(color)
                    .cardName(cardName)
                    .userId(userId)
                    .build();
            growthRepository.saveLearningLog(dailyLearningLog); // 학습 로그 저장
        } else {
            dailyLearningCards.get(0).updateClick(); // 클릭 수 업데이트
        }
    }

    /**
     * 성장 일지를 가져오는 메소드
     *
     * @param creationTime 생성 시간
     * @param userId 사용자 ID
     * @return 성장 일지 DTO 리스트
     */
    public List<GrowthDto> getGrowthList(String creationTime, String userId) {
        List<GrowthDto> growthDtoList = new ArrayList<>();
        List<Growth> growths = growthRepository.findGrowthByTime(creationTime, userId);

        for (Growth growth : growths) {
            GrowthDto growthDto = GrowthDto.builder()
                    .image(getS3FileUrl(growth.getImageUrl(), imageFolder))
                    .cardName(growth.getCardName())
                    .color(growth.getColor())
                    .build();
            growthDtoList.add(growthDto); // DTO 리스트에 추가
        }
        return growthDtoList; // 성장 DTO 리스트 반환
    }

    /**
     * 성장 일지를 생성하는 메소드
     *
     * @param userId 사용자 ID
     * @param childCode 아동 코드
     */
    public void makeGrowth(String userId, String childCode) {
        LocalDateTime now = LocalDateTime.now();
        String creationTime = formatDate(now);

        List<AacCard> aacCards = aacCardRepository.getTodayAacCardList(creationTime, userId);
        List<VideoCard> videoCards = videoCardRepository.getTodayVideoCardList(creationTime, userId);

        GrowthDiary growthDiary = GrowthDiary.builder()
                .childCode(childCode)
                .creationTime(creationTime)
                .imageCardNum(aacCards.size())
                .videoCardNum(videoCards.size())
                .userId(userId)
                .build();
        growthRepository.saveGrowthDiary(growthDiary); // 성장 일지 저장
    }

    /**
     * 성장 일지 DTO 리스트를 가져오는 메소드
     *
     * @param userId 사용자 ID
     * @param childId 아동 ID
     * @return 성장 일지 DTO 리스트
     */
    public List<GrowthDiaryDto> getGrowthDiaryDto(String userId, String childId) {
        List<GrowthDiaryDto> growthDiaryDtoList = new ArrayList<>();
        List<GrowthDiary> growthDiaryList = growthRepository.findGrowthDiaryByUserAndChild(userId, childId);

        for (GrowthDiary growthDiary : growthDiaryList) {
            List<DailyLearningLog> dailyLearningLogs = growthRepository.findLearningLogsByDateAndChild(growthDiary.getCreationTime(), childId, userId);
            List<DailyLearningLogDto> dailyLearningLogDtos = convertToLearningDtoList(dailyLearningLogs);

            GrowthDiaryDto growthDiaryDto = GrowthDiaryDto.builder()
                    .imageCardNum(growthDiary.getImageCardNum())
                    .videoCardNum(growthDiary.getVideoCardNum())
                    .dailyLearningLogDtoList(dailyLearningLogDtos)
                    .creationTime(growthDiary.getCreationTime())
                    .userName(getUserName(growthDiary.getUserId()))
                    .build();
            growthDiaryDtoList.add(growthDiaryDto); // 성장 일지 DTO 리스트에 추가
        }
        return growthDiaryDtoList; // 성장 일지 DTO 리스트 반환
    }

    /**
     * DailyLearningLog 리스트를 DTO 리스트로 변환하는 메소드
     *
     * @param dailyLearningLogs DailyLearningLog 리스트
     * @return DailyLearningLogDto 리스트
     */
    private List<DailyLearningLogDto> convertToLearningDtoList(List<DailyLearningLog> dailyLearningLogs) {
        List<DailyLearningLogDto> dailyLearningLogDtos = new ArrayList<>();
        for (DailyLearningLog learningDiary : dailyLearningLogs) {
            String imageUrl;

            // Card Type에 따라 URL 다르게 설정
            if ("video".equals(learningDiary.getCardType()))
                imageUrl = thumbFolder + learningDiary.getImageUrl();
            else
                imageUrl = imageFolder + learningDiary.getImageUrl();


            DailyLearningLogDto dailyLearningLogDto = DailyLearningLogDto.builder()
                    .image(imageUrl)
                    .cardName(learningDiary.getCardName())
                    .color(learningDiary.getColor())
                    .type(learningDiary.getCardType())
                    .clickCnt(learningDiary.getClickCnt())
                    .build();
            dailyLearningLogDtos.add(dailyLearningLogDto); // DTO 추가
        }
        return dailyLearningLogDtos; // DTO 리스트 반환
    }

    /**
     * 사용자 이름을 가져오는 메소드
     *
     * @param userId 사용자 ID
     * @return 사용자 이름
     */
    private String getUserName(String userId) {
        return userInfoRepository.getUser(userId).get(0).getName();
    }
}