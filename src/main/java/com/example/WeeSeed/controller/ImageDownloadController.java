package com.example.WeeSeed.controller;

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
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class ImageDownloadController {
    public static String encode(String value) {
        StringBuilder encoded = new StringBuilder();
        for (char c : value.toCharArray()) {
            byte[] bytes = String.valueOf(c).getBytes(StandardCharsets.UTF_8);
            for (byte b : bytes) {
                encoded.append(String.format("%%%02X", b));
            }
        }
        return encoded.toString();
    }
    @Value("${raspberry.pi.url}")
    private String raspberryPiUrl;

    @GetMapping("/image/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable("fileName") String fileName) throws UnsupportedEncodingException {


        String url = raspberryPiUrl + fileName;  //파일명이 한글이면 인코딩 때문에 에러남 ~!!주의!!@
        //System.out.println(fileName + ": 여기 :"+ encodedFileName);
       // String url = "http://192.168.0.6:8000/%E1%84%80%E1%85%A1%E1%86%BC%E1%84%8B%E1%85%A1%E1%84%8C%E1%85%B5.jpg"
        try {
            RestTemplate restTemplate = new RestTemplate();
            byte[] imageBytes = restTemplate.getForObject(URI.create(url), byte[].class);

            if (imageBytes == null) {
                return new ResponseEntity<>("Failed to download image", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            InputStream inputStream = new ByteArrayInputStream(imageBytes);
            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageBytes.length);

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error while downloading image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
