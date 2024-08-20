package com.web.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;
    
    // FIXME #REFACT: 가입일 추가
 	private LocalDateTime regDate;
    
    public RefreshToken(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.regDate = LocalDateTime.now();
    }
    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;

        return this;
    }
}