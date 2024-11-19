package org.weeseed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weeseed.dto.NokDto;
import org.weeseed.dto.PathologistDto;
import org.weeseed.entity.Nok;
import org.weeseed.entity.Pathologist;
import org.weeseed.entity.User;
import org.weeseed.repository.UserInfoRepository;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserInfoRepository userInfoRepository;

    /**
     * 사용자 로그인 정보를 확인하는 메서드
     *
     * @param userId   사용자 ID
     * @param password 사용자 비밀번호
     * @return 로그인 성공한 사용자 리스트
     */
    public List<User> checkUserLogIn(String userId, String password) {
        return userInfoRepository.checkUserLogin(userId, password);
    }

    /**
     * 사용자 상태를 조회하는 메서드
     *
     * @param userId 사용자 ID
     * @return 사용자 상태
     */
    public String getUserInfo(String userId) {
        return userInfoRepository.getUserState(userId);
    }

    /**
     * Nok 정보를 업데이트하는 메서드
     *
     * @param nokId Nok ID
     * @return NokDto 객체
     */
    public NokDto updateNok(String nokId) {
        Nok nokInfo = userInfoRepository.getNokInfo(nokId); // Nok 정보 조회
        return NokDto.builder()
                .nokId(nokInfo.getNokId())
                .password(nokInfo.getPassword())
                .email(nokInfo.getEmail())
                .name(nokInfo.getName())
                .build();
    }

    /**
     * Nok 정보를 업데이트하는 메서드
     *
     * @param dto NokDto 객체
     * @return Nok 객체
     */
    public Nok updateNokInfo(NokDto dto) {
        Nok nok = userInfoRepository.getNokInfo(dto.getNokId()); // Nok 정보 조회
        nok.updateNok(dto.getPassword(), dto.getEmail(), dto.getName()); // Nok 정보 업데이트
        return nok;
    }

    /**
     * Pathologist 정보를 업데이트하는 메서드
     *
     * @param pathId Pathologist ID
     * @return Pathologist 객체
     */
    public Pathologist updatePathologist(String pathId) {
        return userInfoRepository.getPathologistInfo(pathId); // Pathologist 정보 조회
    }

    /**
     * Pathologist 정보를 업데이트하는 메서드
     *
     * @param dto PathologistDto 객체
     * @return Pathologist 객체
     */
    public Pathologist updatePathologistInfo(PathologistDto dto) {
        Pathologist pathologist = userInfoRepository.getPathologistInfo(dto.getPathologistId()); // Pathologist 정보 조회
        pathologist.updatePathologist(dto.getPassword(), dto.getEmail(), dto.getOrganizationName(), dto.getName()); // Pathologist 정보 업데이트
        return pathologist;
    }

    /**
     * 사용자를 제거하는 메서드
     *
     * @param constructorId 사용자 ID
     */
    public void removeUser(String constructorId) {
        User user = userInfoRepository.getUser(constructorId).get(0); // 사용자 조회
        userInfoRepository.removeUser(user); // 사용자 제거
    }

    /**
     * Nok을 제거하는 메서드
     *
     * @param constructorId Nok ID
     */
    public void removeNok(String constructorId) {
        Nok nok = userInfoRepository.getNokInfo(constructorId); // Nok 정보 조회
        userInfoRepository.removeNok(nok); // Nok 제거
    }

    /**
     * Pathologist를 제거하는 메서드
     *
     * @param constructorId Pathologist ID
     */
    public void removePathologist(String constructorId) {
        Pathologist pathologist = userInfoRepository.getPathologistInfo(constructorId); // Pathologist 정보 조회
        userInfoRepository.removePathologist(pathologist); // Pathologist 제거
    }
}
