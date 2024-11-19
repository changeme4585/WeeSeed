package org.weeseed.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;


@Service
public class S3Service {

    @Autowired
    private final S3Client s3Client;

    private final String bucketName;


    public S3Service(S3Client s3Client, @Value("${cloud.aws.s3.bucket-name}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    /**
     * S3에 파일 업로드 처리
     *
     * @param fileBytes 업로드할 파일의 바이트 배열
     * @param s3FilePath S3 내 저장 경로
     */
    public void uploadFile(byte[] fileBytes, String s3FilePath) {
        try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3FilePath)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));
        } catch (Exception e) {
            // S3 업로드 중 발생한 오류에 대한 메시지와 원인 제공
            String errorMessage = String.format("Failed to upload file to S3 at path: %s. Error: %s", s3FilePath, e.getMessage());
            throw new RuntimeException(errorMessage, e);
        }
    }

//    public Optional<byte[]> findMostSimilarImageAsBytes(List<String> userImageUrls, MultipartFile image) {
//
//        return null;
//    }

    /* getFiles */
    /* -> ExtendedAacCardService */
}
