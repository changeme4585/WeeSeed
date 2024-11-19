package org.weeseed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.weeseed.FileNameEncryptor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import org.json.JSONObject;
import org.json.JSONException;


import java.io.IOException;
import org.springframework.http.HttpHeaders;

import static com.google.common.io.Files.getFileExtension;

@Service
public class ImageUploadService {

    @Value("${flask.server.url}")
    private String flaskServerUrl;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.s3.folder.temps}")
    private String tempFolder;

    private final S3Client s3Client;
    private final RestTemplate restTemplate;

    public ImageUploadService(S3Client s3Client, RestTemplate restTemplate) {
        this.s3Client = s3Client;
        this.restTemplate = restTemplate;
    }


    public String processImageUpload(MultipartFile image) throws IOException {
        if (image.isEmpty() || image.getOriginalFilename() == null) {
            throw new IllegalArgumentException("유효한 파일을 선택해주세요.");
        }

        String encryptedFileName = encryptFileName(image.getOriginalFilename());
        String tempFilePath = tempFolder + encryptedFileName;
        uploadFileToS3(image, tempFilePath);
        String imageUrl = getS3FileUrl(tempFilePath);

        // 이미지 분류 서비스 호출
        String classificationResult = classifyImageWithFlask(image);

        // 분류 결과에 따라 임시 파일 삭제 및 처리 결과 반환
        deleteFileFromS3(tempFilePath);

        // 분류 결과 처리
        return interpretClassificationResult(classificationResult);
    }

    private String interpretClassificationResult(String classificationResult) {
        try {
            // Flask 서버에서 받은 JSON 응답을 그대로 전달
            JSONObject jsonResponse = new JSONObject(classificationResult);

            // Flask 응답 내용 출력 (디버깅용)
            System.out.println("Flask 서버 응답: " + jsonResponse.toString());

            // JSON 응답을 문자열로 변환하여 반환
            return jsonResponse.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return "{\"error\":\"서버 오류 발생\"}"; // JSON 파싱 실패 시 오류 메시지 반환
        }
    }

    private String encryptFileName(String originalFileName) {
        String fileExtension = getFileExtension(originalFileName);
        return new FileNameEncryptor(originalFileName, "임시카드").getEncryptedFileName() + "." + fileExtension;
    }

    private void uploadFileToS3(MultipartFile file, String filePath) throws IOException {
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build(), RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    private String getS3FileUrl(String filePath) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, filePath);
    }

    private void deleteFileFromS3(String filePath) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build());
    }

    private String classifyImageWithFlask(MultipartFile image) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", image.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(flaskServerUrl + "/analyze-image", requestEntity, String.class);

        return response.getBody();
    }
}
