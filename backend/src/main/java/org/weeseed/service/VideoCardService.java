package org.weeseed.service;

import com.google.common.io.Files;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.weeseed.FileNameEncryptor;
import org.weeseed.dto.VideoCardDto;
import org.weeseed.entity.VideoCard;
import org.weeseed.repository.UserInfoRepository;
import org.weeseed.repository.VideoCardRepository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class VideoCardService {
    @Autowired
    private final VideoCardRepository videoCardRepository;
    @Autowired
    private final UserInfoRepository userInfoRepository;
    @Autowired
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.s3.folder.videos}")
    private String videoFolder;

    @Value("${cloud.aws.s3.folder.thumbs}")
    private String thumbnailFolder;
    /**
     * 비디오 카드 업로드
     *
     * @param video          비디오 카드에 등록된 비디오 파일
     * @param cardName       비디오 카드의 이름
     * @param color          비디오 카드의 색상 카테고리
     * @param childCode      카드가 생성된 계정의 아동 코드
     * @param constructorId  카드가 생성된 계정의 ID
     * @param thumbnail      비디오 카드의 썸네일 이미지
     * @return
     */

    public void saveVideoCard(MultipartFile video, String cardName, String color, String childCode, String constructorId, MultipartFile thumbnail) throws IOException {
        String videoFormat = Files.getFileExtension(video.getOriginalFilename());
        String thumbnailFormat = Files.getFileExtension(thumbnail.getOriginalFilename());

        // 암호화된 파일 이름 생성
        FileNameEncryptor videoName = new FileNameEncryptor(video.getOriginalFilename(), cardName);
        FileNameEncryptor thumbnailName = new FileNameEncryptor(thumbnail.getOriginalFilename(), cardName);
        String videoUrl = videoName.getEncryptedFileName() + "." + videoFormat;
        String thumbnailUrl = thumbnailName.getEncryptedFileName() + "." + thumbnailFormat;

        // 현재 날짜 포맷
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));

        // 사용자 유형 조회
        String userState = userInfoRepository.getUserState(constructorId);
        VideoCard videoCard = VideoCard.builder()
                .cardName(cardName)
                .childId(childCode)
                .creationTime(formattedDate)
                .videoUrl(videoUrl)
                .thumbnailUrl(thumbnailUrl)
                .userId(constructorId)
                .color(color)
                .state(userState)
                .build();

        // Video 카드 객체를 데이터베이스에 저장
        videoCardRepository.saveVideoCard(videoCard);

        // S3에 비디오 파일과 썸네일 이미지 업로드
        uploadFileToS3(video, videoUrl, videoFolder);
        uploadFileToS3(thumbnail, thumbnailUrl, thumbnailFolder);

        // 디버그용 출력
        System.out.println("Card Name: " + cardName);
        System.out.println("Color: " + color);
        System.out.println("Child Code: " + childCode);
    }

    /**
     * 특정 아동에 대한 Video 카드 목록을 조회
     *
     * @param childCode      아동 코드
     * @param constructorId  카드 생성자의 ID
     * @return               List<VideoCardDto>
     */
    public List<VideoCardDto> getVideoCard(String childCode, String constructorId) {
        String userState = userInfoRepository.getUserState(constructorId);
        List<VideoCard> videoCardList = userState.equals("Nok")
                ? videoCardRepository.getNokVideoCardList(childCode)
                : videoCardRepository.getPathVideoCardList(childCode, constructorId);

        List<VideoCardDto> videoCardDtoList = new ArrayList<>(videoCardList.size());
        for (VideoCard videoCard : videoCardList) {
            // S3에서 비디오 및 썸네일 이미지의 URL 생성
            String videoUrl = getS3FileUrl(videoCard.getVideoUrl(), videoFolder);
            String thumbnailUrl = getS3FileUrl(videoCard.getThumbnailUrl(), thumbnailFolder);

            // VideoCardDto 객체 생성
            VideoCardDto videoDto = VideoCardDto.builder()
                    .videoCardId(videoCard.getVideoCardId())
                    .cardName(videoCard.getCardName())
                    .creationTime(videoCard.getCreationTime())
                    .color(videoCard.getColor())
                    .childId(videoCard.getChildId())
                    .userId(videoCard.getUserId())
                    .videoUrl(videoUrl)
                    .thumbnailUrl(thumbnailUrl)
                    .build();

            videoCardDtoList.add(videoDto);
        }

        return videoCardDtoList;

    }

    /**
     * 특정 ID의 Video 카드를 삭제
     *
     * @param cardId 카드 ID
     */
    public void deleteVideoCard(Long cardId) {
        VideoCard videoCard = videoCardRepository.findVideoCardById(cardId);
        if (videoCard == null)
            throw new IllegalArgumentException("해당 ID의 Video 카드를 찾을 수 없음 : " + cardId);
        videoCardRepository.deleteVideoCard(videoCard);
        System.out.println("삭제된 카드 ID: " + cardId);
    }

    /**
     * S3에 파일을 업로드
     *
     * @param file     업로드할 파일
     * @param fileName S3에 저장될 파일 이름
     * @throws IOException
     */
    private void uploadFileToS3(MultipartFile file, String fileName, String folderName) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(folderName + fileName)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    /**
     * S3에서 파일을 가져오는 URL 생성
     *
     * @param fileName S3에 저장된 파일 이름
     * @return S3 파일의 URL
     */
    private String getS3FileUrl(String fileName, String folderName) {
        return String.format("https://%s.s3.amazonaws.com/%s%s", bucketName, folderName, fileName);
    }
}

/**
 * 비디오 카드의 정보를 업데이트
 *
 * @param videoFile      새로운 비디오 파일
 * @param thumbnailImage  새로운 썸네일 이미지 파일
 * @param cardId        카드 ID
 * @param newCardName   새로운 카드 이름
 * @throws IOException
 */
/*

public void updateVideoCard(MultipartFile videoFile, MultipartFile thumbnailImage, Long cardId, String newCardName) throws IOException {
    VideoCard videoCard = videoCardRepository.findById(cardId)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 Video 카드를 찾을 수 없음 : " + cardId));

    if (newCardName != null && !newCardName.isEmpty()) {
        videoCard.setCardName(newCardName);
    }
    if (videoFile != null && !videoFile.isEmpty()) {
        String videoFormat = getFileExtension(videoFile.getOriginalFilename());
        FileNameEncryptor videoName = new FileNameEncryptor(videoFile.getOriginalFilename());
        String videoUrl = videoName.getEncryptedFileName() + "." + videoFormat;

        uploadFileToS3(videoFile, videoUrl);
        videoCard.setVideoUrl(videoUrl);
    }
    if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
        String thumbnailFormat = getFileExtension(thumbnailImage.getOriginalFilename());
        FileNameEncryptor thumbnailName = new FileNameEncryptor(thumbnailImage.getOriginalFilename());
        String thumbnailUrl = thumbnailName.getEncryptedFileName() + "." + thumbnailFormat;

        uploadFileToS3(thumbnailImage, thumbnailUrl);
        videoCard.setThumbnailUrl(thumbnailUrl);
    }

    videoCardRepository.save(videoCard);

    // 디버그용 출력
    System.out.println("업데이트된 카드 이름: " + videoCard.getCardName());
    if (videoFile != null && !videoFile.isEmpty()) {
        System.out.println("새 비디오 URL: " + videoCard.getVideoUrl());
    }
    if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
        System.out.println("새 썸네일 URL: " + videoCard.getThumbnailUrl());
    }
}
 */
