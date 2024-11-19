//package org.weeseed.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import software.amazon.awssdk.services.s3.S3Client;
//
//@Service
//@RequiredArgsConstructor
//public class ImageClassificationService {
//
//    @Value("${cloud.aws.s3.bucket-name}")
//    private String bucketName;
//
//    @Value("${cloud.aws.s3.folder.temps}")
//    private String tempFolder;
//
//    private final S3Client s3Client;
//
//    /**
//     * Python AI 모델을 호출하여 이미지를 분석하는 메서드
//     *
//     * @param s3FileUrl S3에 있는 이미지 파일 URL
//     * @return AI 분석 결과
//     */
//    public String classifyImage(String s3FileUrl) {
//        // Python 서버의 API 엔드포인트 설정
//        String pythonApiUrl = "http://15.165.91.37:5000/analyze-image";
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        // HTTP 요청 헤더 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // 요청 바디에 S3 URL을 JSON 형태로 포함
//        String jsonBody = String.format("{\"imageUrl\": \"%s\"}", s3FileUrl);
//
//        // HTTP 요청 생성
//        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
//
//        // POST 요청을 통해 Python 서버에 S3 URL 전송
//        ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl, requestEntity, String.class);
//
//        // Python 서버에서 반환된 분석 결과 처리
//        if (response.getStatusCode().is2xxSuccessful()) {
//            return "사용 가능";
//        } else {
//            return "분석 실패";
//        }
//    }
//}
//
///*
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.http.MediaType;
//
//...
//
//public String classifyImage(String s3FileUrl) {
//    // S3에서 파일 다운로드
//    String fileName = s3FileUrl.substring(s3FileUrl.lastIndexOf("/") + 1);
//    File tempFile = new File("/tmp/" + fileName);
//
//    // S3에서 파일 다운로드
//    downloadFileFromS3(s3FileUrl, tempFile);
//
//    // Python 서버의 API 엔드포인트 설정
//    String pythonApiUrl = "http://15.165.91.37:5000/analyze-image";
//
//    // HTTP 요청 헤더 설정
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//    // Multipart 형식으로 파일 전송
//    FileSystemResource fileResource = new FileSystemResource(tempFile);
//
//    // 필드 이름을 'file'로 설정하여 MultipartEntityBuilder를 사용
//    HttpEntity<?> requestEntity = new HttpEntity<>(Collections.singletonMap("file", fileResource), headers);
//
//    // POST 요청을 통해 Python 서버에 파일 전송
//    ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl, requestEntity, String.class);
//
//    // Python 서버에서 반환된 분석 결과 처리
//    if (response.getStatusCode().is2xxSuccessful()) {
//        return response.getBody();
//    } else {
//        return "분석 실패";
//    }
//}
//
// */
