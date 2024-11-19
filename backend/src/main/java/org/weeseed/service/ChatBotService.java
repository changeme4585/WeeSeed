package org.weeseed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.weeseed.dto.ChatRequest;
import org.weeseed.dto.ChatResponse;
import org.weeseed.dto.Message;
import org.weeseed.entity.Child;
import org.weeseed.entity.SpeechAccuracy;
import org.weeseed.repository.ChildRepository;
import org.weeseed.repository.SpeechAccuracyRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
/**
 * OpenAI API와 통신
 * 아동의 발음 점수 분석 및 피드백 생성
 */
public class ChatBotService {
    private final ChildRepository childRepository;
    private final SpeechAccuracyRepository speechAccuracyRepository;


    @Value("${openai.model.chat}")
    private String chatModel ;

    @Value("${openai.api.url.chat}")
    private String chatApiURL;

    @Autowired
    @Qualifier("openAiRestTemplate")
    private RestTemplate openAiRestTemplate;


    /**
     * 주어진 프롬프트를 기반으로 ChatGPT와 통신하여 응답 반환
     * 프롬프트로 초기 GPT에게 Role을 부여
     *
     * @param prompt    GPT에게 전달할 요청 문구
     * @return          GPT가 생성한 응답 문자열
     */
    public String chat(String prompt){
        ChatRequest request = new ChatRequest(chatModel, prompt);
        request.getMessages().add(new Message(
                "system",
                "당신은 장애 아동의 발음 평가 결과를 분석하고, 학습을 돕기 위한 피드백을 제공하는 전문가입니다. " +
                        "우리는 다양한 단어를 아동에게 제시하고, 아동이 이 단어들을 따라 말할 때 발음을 평가하여 점수를 부여합니다. " +
                        "주어진 역할은 아동의 발음 점수 리스트를 분석하여, 아동의 학습 상태를 평가하고, 반복되는 어려운 단어 패턴을 특정하며, " +
                        "분석 결과에 따른 구체적인 피드백과 학습 조언을 제공하는 것입니다."));
        try {
            ChatResponse response  = openAiRestTemplate.postForObject(chatApiURL, request, ChatResponse.class);
            return response.getChoices().get(0).getMessage().getContent();
        }catch (Exception e){
            System.out.println("GPT 오류: " + e);
        }
        return "응답을 받을 수 없습니다.";
    }

    /**
     * 특정 아동의 발음 데이터와 관련 정보를 통해 GPT에게 피드백 요청
     *
     * @param childCode 조회할 아동의 고유 코드
     * @return          GPT가 생성한 발음 정확도 분석 결과 문자열
     */
    public String question(String childCode) {
        // childCode로 아동 목록 조회
        List<Child> childList = childRepository.getChilds(childCode);
        if (childList.isEmpty())
            return "해당 아동을 찾을 수 없습니다.";
        Child child = childList.get(0);

        // 초기 prompt 생성
        StringBuilder promptBuilder = new StringBuilder(String.format(
                "아동의 이름은 %s이며, 생일은 %s입니다. %s 장애를 가지고 있습니다. 다음은 이 아동의 발음 평가 점수입니다:\n",
                child.getName(), child.getBirth(), child.getDisabilityType()
        ));

        // 아동의 발음 정확도 데이터 조회
        List<SpeechAccuracy> speechList = speechAccuracyRepository.getSpeechList(childCode);
        if (speechList.isEmpty()) {
            // 데이터가 없는 경우
            promptBuilder.append("\n아동의 발음 평가 데이터가 없습니다. 데이터가 없는 경우에도 학습 방향과 장애 아동의 발음을 향상시키기 위한 일반적인 피드백을 제공합니다.");
        } else {
            // 데이터가 있는 경우
            promptBuilder.append("다음은 이 아동의 발음 평가 점수입니다:\n");
            speechList.forEach(speechAccuracy ->
                    promptBuilder.append(String.format(
                            "- %s: %s점\n",
                            speechAccuracy.getCardName(), speechAccuracy.getScore()
                    ))
            );
            promptBuilder.append("\n위 기록을 바탕으로 아동의 학습상태를 분석(어려워하는 단어들의 패턴을 분석 및 그 원인을 추론)하고 " +
                    "이 장애아동에 대한 수치(점수)위주로 피드백 및 학습방향(분석을 바탕으로)을 한국어로 번역해서 알려줘." +
                    "반복되는 내용을 줄이고 반드시 수치적 통계를 바탕으로 피드백 해주세요.");
        }

        // 최종 prompt 생성 및 GPT 응답 요청
        String chatAnswer = chat(promptBuilder.toString());
        System.out.println("GPT 응답: " + chatAnswer);

        return chatAnswer;
    }
}