/**
 * 리플레시 토큰을 획득하는 함
 */
package com.web.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.demo.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    // 아이디로 부터 리플레시토큰 엔티티 획득
	Optional<RefreshToken> findByUserId(Long userId);
    // 리플레시 토큰으로 부터 리플레시토큰 엔티티 획득
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
}

