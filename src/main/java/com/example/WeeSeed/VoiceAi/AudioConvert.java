package com.example.WeeSeed.VoiceAi;

import java.io.*;

public class AudioConvert {
    public static final String path = "/opt/homebrew/bin/ffmpeg";

    String filepath;
    //ffmpeg에 입력할 명령어를 담는 배열
    private String[] command;

    AudioConvert(byte[] audio) throws FileNotFoundException {
        String TmpFilePath = "/Users/joseungbin/Desktop/forTmpFIle/";
        try(FileOutputStream fos= new FileOutputStream(TmpFilePath+"tmp.3gp")){
            fos.write(audio);
        }catch (IOException e){
            e.printStackTrace();
        }
        // 변환된 WAV 파일 경로

        this.filepath =TmpFilePath+"tmp.3gp";
        this.command = new String[]{ //명령어 바꿔야함
                path,
                "-i",
                filepath,
                TmpFilePath+"tmp.wav"
        };
    }

    public String convertAudio() { //입력받은 경로의 파일을 16kHz로 샘플링한 파일로 덮어쓴다.
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            // 에러와 결과를 따로 스레드로 출력
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream());

            errorGobbler.start();
            outputGobbler.start();

            int exitCode = process.waitFor();
            System.out.println("complete");
            filepath.replace(".3gp",".wav");
            return filepath;


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    class StreamGobbler extends Thread { //로그를 출력하지 않으면 변환이 완료되지 않기에 출력하는 클래스
        private InputStream inputStream;

        public StreamGobbler(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
