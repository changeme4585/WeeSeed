package org.weeseed;

import java.io.*;

/**
 * AudioConverter 클래스는 오디오 파일을 변환하는 기능을 제공한다.
 */
public class AudioConverter {

    // FFmpeg 실행 경로
    public static final String FFMPEG_PATH = "/usr/bin/ffmpeg"; // path

    private String inputFilePath; // 입력 파일 경로
    private String outputFilePath; // 출력 파일 경로
    private String[] command; // FFmpeg 명령어 배열

    /**
     * AudioConverter 생성자.
     * @param audio 변환할 오디오 파일의 바이트 배열
     * @throws FileNotFoundException 파일을 찾을 수 없는 경우 발생
     */
    public AudioConverter(byte[] audio) throws FileNotFoundException {
        String tmpFilePath = "/tmp/";

        // 임시 파일 생성
        createTempAudioFile(audio, tmpFilePath + "tmp.m4a");

        // 입력 및 출력 파일 경로 설정
        this.inputFilePath = tmpFilePath + "tmp.m4a";
        this.outputFilePath = tmpFilePath + "tmp.wav";

        // FFmpeg 명령어 초기화
        this.command = new String[]{
                FFMPEG_PATH,
                "-i",
                inputFilePath,
                outputFilePath
        };
    }

    /**
     * 오디오 변환 메서드.
     * @return 변환된 파일 경로
     */
    public String convertAudio() {
        try {
            // FFmpeg 프로세스 시작
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            // 에러 및 결과 출력을 위한 스레드 시작
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream());

            errorGobbler.start();
            outputGobbler.start();

            // 프로세스 종료 코드 대기
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("변환 완료: " + outputFilePath);
                return outputFilePath; // 변환된 파일 경로 반환
            } else {
                System.out.println("변환 실패, 종료 코드: " + exitCode);
                return null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("프로세스가 중단되었습니다.", e);
        } catch (IOException e) {
            throw new RuntimeException("입출력 오류가 발생했습니다.", e);
        }
    }

    /**
     * 임시 오디오 파일을 생성하는 메서드.
     * @param audio 오디오 바이트 배열
     * @param filePath 생성할 파일 경로
     * @throws FileNotFoundException 파일을 찾을 수 없는 경우 발생
     */
    private void createTempAudioFile(byte[] audio, String filePath) throws FileNotFoundException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(audio); // 바이트 배열을 파일에 기록
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 프로세스 출력 스트림을 처리하는 클래스.
     */
    class StreamGobbler extends Thread {

        private InputStream inputStream; // 입력 스트림

        public StreamGobbler(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line); // 프로세스의 출력 라인 출력
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
