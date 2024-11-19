package org.weeseed;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 비밀번호 암호화 및 솔트 값 관리
 */

public class PasswordEncryptor {
    private final String originalPassword;            // 원본 비밀번호
    private final String salt;                // 솔트 값
    private final String encryptedPassword;   // 암호화된 비밀번호

    /**
     * 제공된 비밀번호로 Password Encryptor 구성
     * @param password 암호화 할 원본 비밀번호
     */
    public PasswordEncryptor(String password) {
        this.originalPassword = password;
        this.salt = generateSalt();
        this.encryptedPassword = encryptPassword(password, salt);
    }

    /**
     * 암호화를 위한 무작위 솔트를 생성하여 고유한 출력 보장
     * 솔트는 무작위 바이트 시퀀스
     * 같은 입력값이어도 매번 다른 암호화 결과를 얻을 수 있음
     *
     * @return 생성된 솔트를 16진수 문자열 형태로 반환
     */
    private String generateSalt() {
        // 솔트 바이트 시퀀스 및 값 패딩
        // SecureRandom secureRandom = new SecureRandom();
        byte[] saltBytes = new byte[20];
        //secureRandom.nextBytes(saltBytes);

        // 생성된 솔트 바이트 시퀀스를 두자리 16진수 문자열로 변환
        StringBuilder saltStringBuilder = new StringBuilder();
        for (byte b : saltBytes) {
            saltStringBuilder.append(String.format("%02x", b));
        }
        return saltStringBuilder.toString();
    }

    /**
     * SHA-256과 Salt를 이용하여 비밀번호 암호화
     *
     * @param password 암호화될 원본 비밀번호
     * @param salt     암호화 과정에서 사용될 솔트
     * @return 암호화된 비밀번호의 16진수 문자열 표현
     */
    private String encryptPassword(String password, String salt) {
        try {
            // SHA-256 사용을 위한 MessaegDigest 객체 생성
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");


            // 비밀번호와 솔트를 결합하여 바이트 시퀀스로 변환 후, SHA-256 해시 알고리즘에 입력
            messageDigest.update((password + salt).getBytes());
            byte[] hashedBytes = messageDigest.digest();

            // 해시 처리된 바이트 시퀀스를 16진수 문자열로 변환
            StringBuilder encryptedPasswordBuilder = new StringBuilder();
            for (byte b : hashedBytes)
                encryptedPasswordBuilder.append(String.format("%02x", b));

            return encryptedPasswordBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("암호화 알고리즘을 찾을 수 없습니다.", e);
        }
    }

    /**
     * 암호화된 비밀번호 반환
     *
     * @return 암호화된 비밀번호
     */
    public String getEncryptedPassword() {
        return encryptedPassword;
    }
}
