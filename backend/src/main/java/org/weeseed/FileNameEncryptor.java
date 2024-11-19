package org.weeseed;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 파일 이름을 암호화하는 기능 제공
 * SHA-256 해싱과 Salting 기법 사용
 * 카드명 라벨링 기능 추가
 */

public class FileNameEncryptor {
    private final String originalFileName;
    private final String salt;
    private final String encryptedFileName;

    /**
     * 제공된 파일의 이름으로 FileName Encryptor 구성
     *
     * @param fileName 암호화 할 원본 파일의 이름
     * @param cardName 카드명 라벨
     */
    public FileNameEncryptor(String fileName, String cardName) {
        this.originalFileName = fileName;
        this.salt = generateSalt();
        this.encryptedFileName = encryptFileName(fileName, cardName, salt);
    }

    /**
     * 암호화를 위한 무작위 솔트를 생성하여 고유한 출력 보장
     * 솔트는 무작위 바이트 시퀀스
     * 같은 입력값이어도 매번 다른 암호화 결과를 얻을 수 있음
     *
     * @return 생성된 솔트를 16진수 문자열 형태로 반환
     */
    private String generateSalt() {
        // 솔트 바이트 시퀀스 및 무작위 값 패딩
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[20];
        random.nextBytes(saltBytes);

        // 생성된 솔트 바이트 시퀀스를 두자리 16진수 문자열로 변환
        StringBuilder sb = new StringBuilder();
        for (byte b : saltBytes)
            sb.append(String.format("%02x", b));

        return sb.toString();
    }

    /**
     * SHA-256과 Salt를 이용하여 파일 이름 암호화
     *
     * @param fileName 암호화될 기존 파일의 이름
     * @param cardName 카드명 라벨
     * @param salt     암호화 과정에서 사용될 솔트
     * @return         암호화된 파일 이름의 16진수 문자열 표현
     */
    private String encryptFileName(String fileName, String cardName, String salt) {
        try {
            // SHA-256 사용을 위한 MessageDigest 객체 생성
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 파일 이름과 솔트를 결합하여 바이트 시퀀스로 변환 후, SHA-256 해시 알고리즘에 입력
            digest.update((fileName + salt).getBytes());

            // 해시 처리된 바이트 시퀀스를 16진수 문자열로 변환
            byte[] hashedBytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            // 파일 확장자 추출
            String extension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
                extension = fileName.substring(dotIndex); // .jpg, .png 등 확장자 포함
            }

            // 카드명과 암호화된 내용, 확장자를 결합하여 반환
            return cardName + "_" + sb.toString() + extension;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }


    /**
     * 암호화된 파일 이름을 반환
     * 파일의 원본 이름과 암호화된 솔트를 기반으로 생성된 문자열
     *
     * @return 16진수 문자열로 표현된 암호화된 파일 이름
     */
    public String getEncryptedFileName() {
        return encryptedFileName;
    }

    /**
     * 암호화되지 않은 원본 파일 이름을 반환
     * 파일이 업로드될 때 사용자가 제공한 파일 이름
     *
     * @return 원본 파일 이름 문자열
     */
    public String getOriginalFileName() {
        return originalFileName;
    }
}
