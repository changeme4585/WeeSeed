package org.weeseed.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.web.multipart.MultipartFile;
import org.weeseed.AudioConverter;

import javax.sound.sampled.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 음성 비교 서비스 클래스
 * 외부 API를 사용하여 음성 파일의 발음 유사성을 평가
 */
@SuppressWarnings("deprecation")
public class VoiceSimilarityService {
    final static String OPEN_API_URL = "http://aiopen.etri.re.kr:8000/WiseASR/PronunciationKor"; // API URL
    final static String ACCESS_KEY = "7f8b6d82-f7b7-4b0d-a824-9df6ecf91817"; // 발급받은 API Key
    final static String LANGUAGE_CODE = "korean"; // 언어 코드
    private AudioConverter audioConverter;


    /**
     * 주어진 스크립트와 음성 파일의 발음 유사성을 확인하는 메서드
     *
     * @param script    비교할 텍스트 스크립트
     * @param audioFile 비교할 음성 파일
     * @return 발음 유사성 점수
     * @throws IOException 파일 처리 중 발생하는 예외
     */
    public String checkSimilarity(String script, MultipartFile audioFile) throws IOException {
        // 음성 파일 변환
        byte[] audio = getFileArray(audioFile);
        AudioConverter converter = new AudioConverter(audio);
        String filepath = converter.convertAudio();
        Path path = Paths.get(filepath);
        byte[] audioBytes;
        audioBytes = Files.readAllBytes(path);
        // API 요청을 위한 데이터 준비
        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();
        Gson gson = new Gson();
        String audioContents = Base64.getEncoder().encodeToString(audioBytes);
        argument.put("language_code", LANGUAGE_CODE);
        argument.put("script", script);
        argument.put("audio", audioContents);
        request.put("argument", argument);

        // API 호출 및 응답 처리
        return callVoiceSimilarityAPI(request);
    }

    /**
     * 음성 유사도 API를 호출하는 메서드
     *
     * @param request API 요청 데이터
     * @return 유사도 점수
     * @throws IOException 파일 처리 중 발생하는 예외
     */
    private String callVoiceSimilarityAPI(Map<String, Object> request) throws IOException {
        URL url;
        Integer responseCode;
        try {
            url = new URL(OPEN_API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", ACCESS_KEY);

            // 요청 데이터 전송
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(new Gson().toJson(request).getBytes(StandardCharsets.UTF_8));
                wr.flush();
            }

            responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return processApiResponse(con);
            } else {
                System.out.println("Response Code: " + responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * API 응답을 처리하여 유사도 점수를 반환하는 메서드
     *
     * @param con API 연결 객체
     * @return 유사도 점수
     * @throws IOException 파일 처리 중 발생하는 예외
     */
    private String processApiResponse(HttpURLConnection con) throws IOException {
        InputStream is = con.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        // 응답을 문자열로 읽기
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // JSON 응답 처리
        JsonObject jsonResponse = new Gson().fromJson(response.toString(), JsonObject.class);
        JsonObject returnObject = jsonResponse.getAsJsonObject("return_object");
        return returnObject.get("return_object").getAsString();
    }

    private byte[] getFileArray(MultipartFile audio) throws IOException {

        ByteArrayInputStream tmp=new ByteArrayInputStream(audio.getBytes());
        AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
        AudioInputStream stream=new AudioInputStream(tmp,format, audio.getBytes().length / format.getFrameSize());

        AudioFormat Wantedformat = new AudioFormat(16000, 16, 2, true, false);
        AudioInputStream targetStream= AudioSystem.getAudioInputStream(Wantedformat,stream);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = targetStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return byteArrayOutputStream.toByteArray();
    }
}