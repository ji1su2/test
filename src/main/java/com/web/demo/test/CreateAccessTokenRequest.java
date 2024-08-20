/**
 * 토큰 생성 요청 담당 dto
 */
package com.web.demo.test;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenRequest {
    private String refreshToken;
}