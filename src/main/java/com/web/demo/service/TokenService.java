package com.web.demo.service;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.demo.config.JwtProperties;
import com.web.demo.entity.RefreshToken;
import com.web.demo.entity.SnsUser;
import com.web.demo.exception.DataNotFoundException;
import com.web.demo.repository.RefreshTokenRepository;

@Service
public class TokenService {
	@Autowired
	private TokenProvider tokenProvider;
	@Autowired
	private JwtProperties jwtProperties;
	@Autowired
	private UserService userService;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	// 리프레시 토큰 => 엑세스 토큰
    public String createNewAccessToken(String refreshToken) {
        // 리플레시 토큰 자체 유효성체크
        if(!tokenProvider.isValidToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }
        // 리프세시 토큰이 디비상에 존재하는가?
        if(!tokenProvider.isValidRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }
        // 리프레시 토큰 => 디비조회 => 유저 아이디 획득
        Long userId = findByRefreshToken(refreshToken).getUserId();
        // 유저 아이디 => 사용자 획득
        SnsUser user = userService.findById(userId);
        // 사용자정보, 만료시간 => 엑세스 토큰
        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }    
    // 리플레시 토큰 => 리플레시 토큰 : 리프레시 토큰 유효성 체크
    public RefreshToken findByRefreshToken(String refreshToken) {
    	Optional<RefreshToken> opt = this.refreshTokenRepository.findByRefreshToken(refreshToken);
		if(opt.isPresent()) {
			return opt.get();
		}
		// 커스텀 예외 상황
		throw new DataNotFoundException("user not found");
		//return refreshTokenRepository.findByRefreshToken(refreshToken)
        //        .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
    
    // 리플레시 토큰 저장
    public void saveRefreshToken(Long id, String token) {
    	System.out.println(id + " " + token);
    	RefreshToken rtoken = new RefreshToken(id, token);
    	this.refreshTokenRepository.save(rtoken);
    }
    
    public RefreshToken findByUserId(Long uid) {
    	Optional<RefreshToken> opt = this.refreshTokenRepository.findByUserId(uid);
		if(opt.isPresent()) {
			return opt.get();
		}
		// 커스텀 예외 상황
		throw new DataNotFoundException("user not found");
		//return refreshTokenRepository.findByRefreshToken(refreshToken)
        //        .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
