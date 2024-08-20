/**
 * 회원 가입폼 유효성 검사 용도
 */
package com.web.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {
	// 5 <= username <= 32 : 테이블상의 컬럼 크기에 따라 제한될수 있다
	@Size(min = 5, max = 32)
	// 아이디 중복 체크 기능을 추가할 수 있다
	@NotEmpty(message = "사용자 ID는 고유한 값으로 반드시 입력하세요")
	private String username;
	
	// 차후, 사이즈는 8 ~16, 영소, 영대, 특수문자, 숫자 반드시 포함 => 정규식 체크
	@NotEmpty(message = "비밀번호 필수 입력 항목")
	private String password1;
	
	@NotEmpty(message = "비밀번호 확인 필수 입력 항목")
	private String password2;
	
	@NotEmpty(message = "이메일 필수 입력 항목")
	@Email
	private String email;
}