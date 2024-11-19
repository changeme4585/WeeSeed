package org.weeseed.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.weeseed.entity.Child;
import org.weeseed.repository.ChildRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChildService {

    @Autowired
    private final ChildRepository childRepository;

    /**
     * 사용자ID를 기반으로 아동 목록을 조회
     *
     * @param userId 사용자 ID
     * @return 아동 목록
     */
    public List<Child> getChildByUser(String userId) {
        return childRepository.getChildByUser(userId);
    }
}