package com.example.WeeSeed.VoiceAi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
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

public class VoiceCompareApi {
    final static String openApiURL = "http://aiopen.etri.re.kr:8000/WiseASR/PronunciationKor";
    final static String accessKey = "7f8b6d82-f7b7-4b0d-a824-9df6ecf91817"; // 발급받은 API Key
    String languageCode = "korean";     // 언어 코드

//    String audioFilePath = "AUDIO_FILE_PATH";  // 녹음된 음성 파일 경로


    public String checkSimilarity(String script, MultipartFile audioFile) throws IOException {
        byte[] audio=getFileArray(audioFile);
        AudioConvert converter=new AudioConvert(audio);
        String filepath= converter.convertAudio();
        Path path = Paths.get(filepath);
        byte[] audioBytes;
        audioBytes = Files.readAllBytes(path);
        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();
        Gson gson = new Gson();
        String audioContents = Base64.getEncoder().encodeToString(audioBytes);
        argument.put("language_code", languageCode);
        argument.put("script", script);
        argument.put("audio", audioContents);
        request.put("argument", argument);
        URL url;
        Integer responseCode = null;
        String responBody;
        try {
            url = new URL(openApiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", accessKey);
            System.out.println(argument.get("script"));
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(gson.toJson(request).getBytes(StandardCharsets.UTF_8));
            wr.flush();
            wr.close();

            System.out.println(request.get("argument"));

            responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

                // Reading the response into a String
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Convert the response to a string
                String responseBody = response.toString();
                System.out.println(responseBody);
                // Using Gson to parse the response to JsonObject
                gson = new Gson();
                JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

                // Extracting the nested "return_object" object
                JsonObject returnObject = jsonResponse.getAsJsonObject("return_object");

                // Extracting the "score" value from the "return_object"
                //Double score=returnObject.get("score").getAsDouble();
                //score=score*20;
                String score=returnObject.get("return_object").getAsString();
                return score.toString();

            } else {
                System.out.println("Response Code: " + responseCode);
            }
//            responseCode = con.getResponseCode();
//            InputStream is = con.getInputStream();
//            byte[] buffer = new byte[is.available()];
//            int byteRead = is.read(buffer);
//            responBody = new String(buffer);
//
//            System.out.println("[responseCode] " + responseCode);
//            System.out.println("[responBody]");
//            System.out.println(responBody);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
