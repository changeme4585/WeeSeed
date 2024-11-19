package org.weeseed.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.weeseed.FileNameEncryptor;
import org.weeseed.dto.AacCardDto;
import org.weeseed.entity.AacCard;
import org.weeseed.repository.AacCardRepository;
import org.weeseed.repository.UserInfoRepository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.io.Files.getFileExtension;

@Service
@Transactional
@RequiredArgsConstructor
public class AacCardService {

    @Autowired
    private final AacCardRepository aacCardRepository;
    @Autowired
    private final UserInfoRepository userInfoRepository;
    @Autowired
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.s3.folder.images}")
    private String imageFolder;

    @Value("Audios/")
    private String audioFolder;

    /**
     * 주어진 데이터로 새로운 AAC 카드 저장 (S3 버전)
     *
     * @param image         AAC 카드에 등록된 이미지 파일
     * @param cardName      AAC 카드의 이름
     * @param audio         AAC 카드에 등록된 음성 파일
     * @param color         AAC 카드의 색상 카테고리
     * @param childCode     카드가 생성된 계정의 아동 코드
     * @param constructorId 카드가 생성된 계정의 ID (보호자 또는 재활사)
     * @param share         카드 생성자가 설정한 공유 여부
     * @throws IOException
     */
    public void saveAacCard(MultipartFile image, String cardName, MultipartFile audio, String color, String childCode, String constructorId, int share) throws IOException {

        // 파일 확장자 추출
        String imageFormat = getFileExtension(image.getOriginalFilename());
        String voiceFormat = getFileExtension(audio.getOriginalFilename());

        // 암호화된 파일 이름 생성
        FileNameEncryptor imageName = new FileNameEncryptor(image.getOriginalFilename(), cardName);
        FileNameEncryptor audioName = new FileNameEncryptor(audio.getOriginalFilename(), cardName);
        String imageUrl = imageName.getEncryptedFileName() + "." + imageFormat;
        String voiceUrl = audioName.getEncryptedFileName() + "." + voiceFormat;

        // 현재 날짜 포맷
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));

        // 사용자 유형 조회
        String userState = userInfoRepository.getUserState(constructorId);
        AacCard aacCard = AacCard.builder()
                .cardName(cardName)
                .creationTime(formattedDate)
                .color(color)
                .childId(childCode)
                .constructorId(constructorId)
                .imageUrl(imageUrl)
                .voiceUrl(voiceUrl)
                .share(share)
                .clickCnt(0)
                .state(userState)
                .build();

        // AAC 카드 객체를 데이터베이스에 저장
        aacCardRepository.saveAacCard(aacCard);

        // S3에 이미지와 오디오 파일 업로드
        uploadFileToS3(image, imageUrl, imageFolder);
        uploadFileToS3(audio, voiceUrl, audioFolder);

        // 디버그용 출력
        System.out.println("Card Name: " + cardName);
        System.out.println("Color: " + color);
        System.out.println("Child Code: " + childCode);
    }

    public List<AacCardDto> getAacCard(String childCode, String constructorId) {
        // 사용자 유형에 따라 카드 목록 조회
        String userState = userInfoRepository.getUserState(constructorId);
        List<AacCard> aacCardList = userState.equals("Nok")
                ? aacCardRepository.getNokAacCardList(childCode)
                : aacCardRepository.getPathAacCardList(childCode, constructorId);

        List<AacCardDto> aacDtoList = new ArrayList<>(aacCardList.size());

        for (AacCard aacCard : aacCardList) {
            // S3에서 이미지와 오디오 파일의 URL 생성
            String imageUrl = getS3FileUrl(aacCard.getImageUrl(), imageFolder);
            String voiceUrl = getS3FileUrl(aacCard.getVoiceUrl(), audioFolder);

            // AacDto 객체 생성
            AacCardDto aacDto = AacCardDto.builder()
                    .aacCardId(aacCard.getAacCardId())
                    .cardName(aacCard.getCardName())
                    .creationTime(aacCard.getCreationTime())
                    .color(aacCard.getColor())
                    .childId(aacCard.getChildId())
                    .constructorId(aacCard.getConstructorId())
                    .image(imageUrl)
                    .voice(voiceUrl)
                    .build();

            aacDtoList.add(aacDto);
        }

        return aacDtoList;
    }

    public void deleteAacCard(Long cardId) {
        AacCard aacCard = aacCardRepository.findAacCardById(cardId);
        if (aacCard == null)
            throw new IllegalArgumentException("해당 ID의 AAC 카드를 찾을 수 없음 : " + cardId);
        aacCardRepository.deleteAacCard(aacCard);
        System.out.println("삭제된 카드 ID: " + cardId);
    }

    public void updateAacCard(MultipartFile image, String childCode, String constructorId, Long cardId, String cardName, String newCardName) throws IOException {
        AacCard aacCard = aacCardRepository.findAacCardById(cardId);
        if (aacCard == null)
            throw new IllegalArgumentException("해당 ID의 AAC 카드를 찾을 수 없음 : " + cardId);

        if (newCardName != null && !newCardName.isEmpty())
            aacCard.setCardName(newCardName);
        if (image != null && !image.isEmpty()) {
            String imageFormat = getFileExtension(image.getOriginalFilename());
            FileNameEncryptor imageName = new FileNameEncryptor(image.getOriginalFilename(), cardName);
            String imageUrl = imageName.getEncryptedFileName() + "." + imageFormat;

            uploadFileToS3(image, imageUrl, imageFolder);
            aacCard.setImageUrl(imageUrl);
        }
        aacCardRepository.updateAacCard(aacCard);

        System.out.println("업데이트된 카드 이름: " + aacCard.getCardName());
        if (image != null && !image.isEmpty()) {
            System.out.println("새 이미지 URL: " + aacCard.getImageUrl());
        }
    }

    // S3에 파일 업로드
    private void uploadFileToS3(MultipartFile file, String fileName, String folderName) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(folderName + fileName) // S3의 폴더 경로 포함
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    // S3에서 파일을 가져오는 URL 생성
    private String getS3FileUrl(String fileName, String folderName) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + folderName + fileName;
    }

}
