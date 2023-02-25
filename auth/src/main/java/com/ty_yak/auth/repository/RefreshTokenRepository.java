package com.ty_yak.auth.repository;

import com.ty_yak.auth.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserIdAndId(Long userId, Long tokenId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE refresh_tokens SET token = ?1 WHERE id = ?2", nativeQuery = true)
    void update(String token, Long tokenId);

    @Transactional
    void deleteByUserIdAndId(Long userId, Long tokenId);

    @Transactional
    void deleteAllByExpiredAtBefore(LocalDateTime dateTime);
}
