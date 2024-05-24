package com.example.WeeSeed.service;


import com.example.WeeSeed.dto.NokDto;
import com.example.WeeSeed.entity.Nok;
import com.example.WeeSeed.repository.UserInfoRepository;
import com.example.WeeSeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class modifyUserInfoService {
    private final UserInfoRepository userRepository;
    public Nok updateNok(NokDto dto){
        Nok nok = userRepository.getNokInfo(dto.getNokId());
        nok.updateNok(dto.getPassword(),dto.getEmail(), dto.getName());
        return nok;
    }
}
