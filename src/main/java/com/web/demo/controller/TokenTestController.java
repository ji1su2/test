/**
 * 토큰 컨트롤러
 */
package com.web.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.demo.service.TokenService;
import com.web.demo.test.CreateAccessTokenRequest;
import com.web.demo.test.CreateAccessTokenResponse;

@RequiredArgsConstructor
@RestController
public class TokenTestController {

    private final TokenService tokenService;
    
    /**
     * 실제 요청 => 리플레시 토큰 획득 => 새로운 엑세스 토큰 생성
     * @param request
     * @return
     */
    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}