/**
 * 토큰 생성 응답 담당 dto
 */
package com.web.demo.test;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateAccessTokenResponse {
    private String accessToken;
}
