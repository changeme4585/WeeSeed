package org.weeseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.weeseed.entity.User;

import java.util.Optional;

/**
 * 사용자 정보 관리를 위한 리포지토리 인터페이스
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 사용자 ID로 사용자 정보 조회
     *
     * @param userId 사용자 ID
     * @return Optional<User> 사용자 정보
     */
    Optional<User> findByUserId(String userId);

    //    Optional<Nok> findByNokId(String nokId);
    //    Optional<Pathologist> findByPath(String pathId);
}
