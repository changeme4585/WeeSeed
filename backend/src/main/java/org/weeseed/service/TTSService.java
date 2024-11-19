package org.weeseed.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Service
public class TTSService {

    @Value("${WHISPER_SERVER_URL}")
    private String externalTTSUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<byte[]> getTTS(String cardName) {
        String url = externalTTSUrl + "/api/tts?cardName=" + cardName;
        return restTemplate.exchange(url, HttpMethod.POST, null, byte[].class);
    }


//    public ResponseEntity<String> soundCompare(String cardName, MultipartFile audioFile) {
//        String url = externalTTSUrl + "/api/soundcompare";
//
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("card_name", cardName);
//        body.add("audio", convertToFileResource(audioFile));
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//    }
//
//    private Resource convertToFileResource(MultipartFile file) {
//        try {
//            return new ByteArrayResource(file.getBytes()) {
//                @Override
//                public String getFilename() {
//                    return file.getOriginalFilename();
//                }
//            };
//        } catch (IOException e) {
//            throw new RuntimeException("파일 변환 중 오류 발생", e);
//        }
//    }
}