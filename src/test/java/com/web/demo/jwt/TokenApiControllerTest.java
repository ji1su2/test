package com.web.demo.jwt;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.demo.config.JwtProperties;
import com.web.demo.entity.RefreshToken;
import com.web.demo.entity.SnsUser;
import com.web.demo.repository.RefreshTokenRepository;
import com.web.demo.repository.UserRepository;
import com.web.demo.test.CreateAccessTokenRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokenApiControllerTest {
	
	// 스프링 프레임워크에서 제공하는 웹 애플리케이션 테스트용 라이브러리를 의미
	// HTTP 요청을 작성하고 컨트롤러의 응답을 검증할 수 있습니다. 
    @Autowired
    protected MockMvc mockMvc;
    
    // 리플렉션을 활용해서 객체로부터 Json 형태의 문자열을 만들어냅 -> 요청담아서 처리
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    
    // 테스트 메서드 실행 이전에 수행
    @BeforeEach
    public void mockMvcSetUp() {
    	System.out.println("mockMvcSetUp() ");
    	// MockMvc 객체 생성
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        // 원활한 테스트를 위해서 계속 유저를 제거
        userRepository.deleteAll();
    }

    @DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
    @Test
    public void createNewAccessToken() throws Exception {
        // given :주
        final String url = "/api/token";
        // 사용자
        SnsUser testUser = new SnsUser();
    	testUser.setUsername("quest3");
    	testUser.setPassword("1");
    	testUser.setEmail("quest3@a.com");
    	userRepository.save( testUser );
    	
    	// 리플레시 토큰 생성
        String refreshToekn = JwtFactory.builder()
                .claims(Map.of("id", testUser.getId()))
                .build()
                .createToken(jwtProperties);
        
        // 리플레시 토큰 저장
        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToekn));
        
        // 요청 생성
        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        // 데이터 세팅
        request.setRefreshToken(refreshToekn);
        // 요청시 전달할 형태 획득 => 객체 직렬화를 통해 문자열로 구성
        final String requestBody = objectMapper.writeValueAsString(request);
        System.out.println("requestBody:" + requestBody);
        
        // when  요청및 결과 획득, json 형태 요청
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then 응답코드가 201인지 확인 옥 엑세스 토큰이 비어 있지 않은지 확인
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

}