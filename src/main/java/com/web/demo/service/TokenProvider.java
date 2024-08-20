package com.web.demo.service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.web.demo.config.JwtProperties;
import com.web.demo.entity.RefreshToken;
import com.web.demo.entity.SnsUser;
import com.web.demo.exception.DataNotFoundException;
import com.web.demo.repository.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenProvider {
	@Autowired
	private JwtProperties jwtProperties;
	//@Autowired
	//private UserService userService;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	// 사용자 정보 + 만료시간 => 토큰 생성
    public String generateToken(SnsUser user, Duration expiredAt) {
        Date now = new Date();
        return createToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }
    public String reverseGenerateToken(SnsUser user, Duration expiredAt) {
        Date now = new Date();
        return createToken(new Date(now.getTime() - expiredAt.toMillis()), user);
    }
    // 토큰 생성 내부 함수
    private String createToken(Date expiry, SnsUser user) {
    	// 현재 시간
        Date now = new Date();
        // 토큰 생성
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)	// typ, 토큰 타입 지정(JWT), 
                .setIssuer(jwtProperties.getIssuer())	// 사용자
                .setIssuedAt(now)						// 사용자 현재시간
                .setExpiration(expiry)					// 만료시간
                .setSubject(user.getEmail())			// 제목
                .claim("id", user.getId())				// 클레임 : 사용자 관리 아이디 (고유값)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())	// 암호화방식, 비밀키
                .compact();
    }
    // 토큰 유효성 검증
    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
        	return false;
        }
    }
    // 유효한 토큰 => 사용자 아이디 획득
    public Long getUserId(String token) {
    	Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }
    // 클레임 정보 획득 내부 함수 
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
    // 입력 : 토큰
    // 출력 : 인증 정보를 담은 Authentication(어썬~트케이션) 객체 
    public Authentication getAuthentication(String token) {
    	// 토큰 => 토큰의 정보를 접근할수 있는 클레임 정보 획득
        Claims claims = getClaims(token);        
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        // 아래 정보를 가진 Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(
			new User(claims.getSubject(), "", authorities), // 이메일, 권한 정보를 담은 보안유저객
			token, // 토큰
			authorities	// 권리정보
			);
    }
    
    // 리프에시 토큰 유효성    
    public boolean isValidRefreshToken(String refreshToken) {
    	Optional<RefreshToken> opt = this.refreshTokenRepository.findByRefreshToken(refreshToken);
		if(opt.isPresent()) {
			return true;
		}
		return false;
    }
    
}
