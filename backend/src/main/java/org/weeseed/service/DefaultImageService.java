package org.weeseed.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.weeseed.dto.DefaultImageDto;
import org.weeseed.entity.DefaultImage;
import org.weeseed.repository.DefaultImageRepository;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultImageService {

    private final S3Client s3Client;
    private final DefaultImageRepository defaultImageRepository;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.s3.folder.default}")
    private String folderPath;

    /**
     * S3에서 기본 이미지를 불러와서 바이트 배열로 반환
     *
     * @param imageName 이미지 이름
     * @return 이미지의 바이트 배열
     * @throws IOException 이미지 읽기 오류
     */
    public byte[] getDefaultImage(String imageName) throws IOException {
        String key = folderPath + imageName + ".jpg"; // S3 내 저장 경로 구성
        logPath(key);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try (InputStream s3InputStream = s3Client.getObject(getObjectRequest);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = s3InputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (S3Exception e) {
            String errorMessage = String.format("Failed to load image from S3 at path: %s. Error: %s", key, e.awsErrorDetails().errorMessage());
            throw new RuntimeException(errorMessage, e);
        }
    }

    public byte[] getDefaultImageVoice(String voiceName) throws IOException {
        String key = folderPath + voiceName + ".m4a"; // S3 내 저장 경로 구성
        logPath(key);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try (InputStream s3InputStream = s3Client.getObject(getObjectRequest);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = s3InputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (S3Exception e) {
            String errorMessage = String.format("Failed to load voice from S3 at path: %s. Error: %s", key, e.awsErrorDetails().errorMessage());
            throw new RuntimeException(errorMessage, e);
        }
    }


    private void logPath(String key) {
        System.out.println("S3 Image Key : " + key);
        System.out.println("Full Path : " + bucketName + "/" + key);
    }


    // 사용자의 기본 이미지 목록을 DTO로 변환하여 반환
    public List<DefaultImageDto> getUserDefaultImageList(String constructorId) throws IOException {
        List<DefaultImage> defaultImageList = defaultImageRepository.findDefaultImagesByUserId(constructorId);

        return defaultImageList.stream()
                .map(defaultImage -> {
                    try {
                        return DefaultImageDto.builder()
                                .cardName(defaultImage.getCardName())
                                .cardImage(getDefaultImage(defaultImage.getCardName()))
                                .cardVoice(getDefaultImageVoice(defaultImage.getCardName())) // 음성 파일 추가
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    // 기본 이미지를 삭제
    public void deleteDefaultImage(String constructorId, String cardName) {
        DefaultImage defaultImage = defaultImageRepository.findDefaultImageByCardName(constructorId, cardName);
        defaultImageRepository.deleteDefaultImage(defaultImage);
    }

    // 기본 이미지의 카드 이름을 업데이트
    public void updateDefaultCard(String constructorId, String cardName, String newCardName) {
        DefaultImage defaultImage = defaultImageRepository.findDefaultImageByCardName(constructorId, cardName);
        defaultImage.updateCardName(newCardName);
    }
}
