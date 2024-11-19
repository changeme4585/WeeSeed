package org.weeseed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.weeseed.dto.ExtendedAacCardDto;
import org.weeseed.entity.ExtendedAacCard;
import org.weeseed.repository.ExtendedAacCardRepository;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExtendedAacCardService {

    @Value("${flask.server.url}")
    private String flaskServerUrl;

    private static final String S3_BASE_URL = "https://weeseed-uploads-ap-northeast-2.s3.amazonaws.com/";
    private final ExtendedAacCardRepository extendedAacCardRepository;
    private final RestTemplate restTemplate;

    private String convertToFullUrl(String imagePath) {
        if (imagePath.startsWith(S3_BASE_URL))
            return imagePath; // 이미 전체 URL이면 그대로 반환
        else
            // 기본 URL과 이미지 경로를 결합하여 전체 URL을 반환
            return S3_BASE_URL + imagePath;
    }

    public String addExtendedImage(Long representativeCardId, String cardName, String cardPath) throws IOException {
        // 연결된 카드 목록 조회
        List<ExtendedAacCard> connectedCards = extendedAacCardRepository.findByRepCardId(representativeCardId);

        // 연결된 카드 경로 리스트 생성
        List<String> connectedCardPaths = connectedCards.stream()
                .map(ExtendedAacCard::getImageUrl)
                .toList();

        connectedCards.stream()
                .map(card -> new ExtendedAacCardDto(
                        card.getExtendedCardId(),
                        card.getRepCardId(),
                        card.getImageUrl(),
                        card.getOrder_num()))
                .collect(Collectors.toList());

        // 파이썬 서버에 요청 전송
        String responseJson = findSimilarImage(representativeCardId, cardName, cardPath, connectedCardPaths);

        // JSON 응답에서 best_match 값 추출
        String similarImageUrl = extractBestMatchUrl(responseJson);
        System.out.println("Parsed URL without quotes: " + similarImageUrl);

        // 유사 이미지 URL 반환
        if (similarImageUrl != null) {
            ExtendedAacCard newExtendedCard = ExtendedAacCard.builder()
                    .repCardId(representativeCardId)
                    .imageUrl(similarImageUrl)
                    .order_num(connectedCards.size() + 1)
                    .build();

            extendedAacCardRepository.save(newExtendedCard);

            return similarImageUrl;
        } else {
            return "유사한 이미지가 없습니다."; // 더 이상 추가할 이미지가 없다
        }
    }

    private String findSimilarImage(Long representativeCardId, String cardName, String imagePath, List<String> connectedCardPaths) throws IOException {
        // JSON 요청 본문 생성
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(new SimilarImageRequest(representativeCardId, cardName, imagePath, connectedCardPaths));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(flaskServerUrl + "/find-similar-image", requestEntity, String.class);

        // 응답 처리
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();  // 유사한 이미지 URL 반환
        } else {
            return null;
        }
    }

    public List<ExtendedAacCardDto> getExtendedAacCards(Long representativeCardId) {
        // repCardId로 확장 카드 목록 조회
        List<ExtendedAacCard> connectedCards = Optional.ofNullable(extendedAacCardRepository.findByRepCardId(representativeCardId))
                .orElse(Collections.emptyList());

        return connectedCards.stream()
                .map(card -> new ExtendedAacCardDto(
                        card.getExtendedCardId(),
                        card.getRepCardId(),
                        card.getImageUrl(),
                        card.getOrder_num()))
                .collect(Collectors.toList());
    }

    @Data
    public static class SimilarImageRequest {
        private Long representativeCardId;
        private String cardName;
        private String imagePath;
        private List<String> connectedCardPaths;

        public SimilarImageRequest(Long representativeCardId, String cardName, String imagePath, List<String> connectedCardPaths) {
            this.representativeCardId = representativeCardId;
            this.cardName = cardName;
            this.imagePath = imagePath;
            this.connectedCardPaths = connectedCardPaths;
        }
    }

    private String extractBestMatchUrl(String responseJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseJson);
            String bestMatchUrl = jsonNode.get("best_match").asText();

            // 한글 인코딩 처리
            return URLDecoder.decode(bestMatchUrl, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
