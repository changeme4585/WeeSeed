package org.weeseed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @Autowired
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    // 임시 지정 불확실 요소
    @Value("${cloud.aws.s3.folder.images}")
    private String folderName;


    public FileUploadController(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * API를 통해 업로드된 파일을 처리하고 S3의 지정된 폴더에 저장하는 메서드
     *
     * @param file 업로드할 Multipart 파일
     * @return 업로드 결과 메시지를 포함한 ResponseEntity
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {

        // 파일이 비어있는지 확인
        if (file.isEmpty()) {
            return new ResponseEntity<>("파일을 선택해주세요.", HttpStatus.BAD_REQUEST);
        }

        String fileName = file.getOriginalFilename();  // 파일의 원본 이름을 가져옴
        if (fileName == null) {
            return new ResponseEntity<>("유효하지 않은 파일 이름입니다.", HttpStatus.BAD_REQUEST);
        }

        try {
            // S3 내의 폴더 경로 + 파일 이름으로 S3에 저장될 파일 경로 설정
            String filePathInS3 = folderName + fileName;

            // S3에 파일 업로드를 위한 요청 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePathInS3)
                    .build();

            // 파일을 S3에 업로드
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return new ResponseEntity<>("File uploaded successfully to S3.", HttpStatus.OK);

        } catch (IOException e) {
            // 업로드 도중 예외가 발생할 경우
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}