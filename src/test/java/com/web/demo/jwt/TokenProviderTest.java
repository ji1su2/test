package com.web.demo.jwt;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.web.demo.config.JwtProperties;
import com.web.demo.entity.SnsUser;
import com.web.demo.repository.UserRepository;
import com.web.demo.service.TokenProvider;

import io.jsonwebtoken.Jwts;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TokenProviderTest {
	
	@Autowired
    private TokenProvider tokenProvider;
	
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;
    
    @Test
    void testMain() {
    	getProperties();
    	//generateToken();
    	//validToken_invalidToken();
    	//validToken_validToken();
    	//getAuthentication();
    	//getUserId();
    }
    void getProperties() {
    	System.out.println( jwtProperties.getSecretKey() );
    	System.out.println( jwtProperties.getIssuer() );
    	
    }

    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    //@Test
    void generateToken() {
    	/**
    	 * 테스트 기준 
    	 * given : 토큰에 유저 정보 추가를 위한 테스트 유저 생성
    	 * when  : 토큰 제공자의 generateToken 메소드 호출하여 토큰 생성
    	 * then  : jjwt 라이브러리를 이용하여 토큰 복호화 처리 -> 복호화된 아이디와, 토큰 생성시 사용한 아이디 일치여부확인 
    	 */
        // given
    	SnsUser testUser = new SnsUser();
    	testUser.setUsername("quest101");
    	testUser.setPassword("1");
    	testUser.setEmail("quest101@a.com");
    	userRepository.save( testUser );

        // when
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));
        System.out.println("토큰 : " + token);
        
        // then
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);
        System.out.println("userId : " + userId);
        System.out.println("testUser.getId() : " + testUser.getId());

        assertThat(userId).isEqualTo(testUser.getId());
    }
    
    // 여기서 부터 수업

    @DisplayName("validToken(): 만료된 토큰인 경우에 유효성 검증에 실패한다.")
    //@Test
    void validToken_invalidToken() {
        // given : 만료된 토큰 생성
        String token = JwtFactory.builder()
        		// 현재 시간에 7일 차이 시간을 빼서 과거형 만료시간을 가진 토큰을 생성
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        // when : 토큰 유효성 검증
        boolean result = tokenProvider.isValidToken(token);

        // then : 유효한 토큰이 아님을 검증
        assertThat(result).isFalse();
    }


    @DisplayName("validToken(): 유효한 토큰인 경우에 유효성 검증에 성공한다.")
    //@Test
    void validToken_validToken() {
        // given : 토큰 생성
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        // when : 검증
        boolean result = tokenProvider.isValidToken(token);

        // then : 유효함을 확인
        assertThat(result).isTrue();
    }


    @DisplayName("getAuthentication(): 토큰 기반으로 인증정보를 가져올 수 있다.")
    //@Test
    void getAuthentication() {
        // given : 이메일 정보(고유한)를 넣어서 토큰 획득
        String userEmail = "quest100@a.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        // when : 토큰을 통해서 인증객체 획득
        Authentication authentication = tokenProvider.getAuthentication(token);

        // then : 인증객체에서 중요한(Principal) 정보(UserDetails)를 획득 -=> User 생성시(getSubject() 사용:이메일  
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")
    //@Test
    void getUserId() {
        // given
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);
        
        // when
        Long userIdByToken = tokenProvider.getUserId(token);
        
        System.out.println(token);
        System.out.println(userIdByToken);
        System.out.println(userId);
        // then
        assertThat(userIdByToken).isEqualTo(userId);
    }

}
