package org.weeseed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
/*
    GET /image/get/{fileName}
 */
public class ImageDownloadController {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.s3.folder.images}")
    private String folderName;

    /**
     * 파일명을 받아 S3에서 이미지를 다운로드하는 메서드
     *
     * @param fileName S3에서 다운로드할 이미지 파일명
     * @return 이미지 바이너리 데이터와 헤더를 포함한 ResponseEntity
     */
    @GetMapping("/get/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable("fileName") String fileName) {
        try {
            // S3에서 파일 객체 가져오기
            String objectKey = folderName + "/" + fileName;  // 폴더가 있을 경우 파일 경로 지정
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            // S3에서 파일 스트림 가져오기
            ResponseInputStream<GetObjectResponse> s3ObjectInputStream = s3Client.getObject(getObjectRequest);
            byte[] imageBytes = s3ObjectInputStream.readAllBytes();

            // 이미지가 없을 때 처리
            if (imageBytes == null || imageBytes.length == 0) {
                return new ResponseEntity<>("이미지를 다운로드하지 못했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // InputStream을 리소스로 변환
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(imageBytes));

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);  // 이미지 타입에 맞게 변경 필요
            headers.setContentLength(imageBytes.length);

            // 성공적으로 이미지 반환
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        } catch (IOException e) {
            // 예외 처리 및 에러 메시지 반환
            return new ResponseEntity<>("이미지 다운로드 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
