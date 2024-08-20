package com.web.demo.dto;

import groovy.transform.ToString;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class PostForm {
	@NotEmpty(message="제목은 반드시 입력해야 하는 필수 항목입니다.")
	// 디비상 테이블의 해당 컬럼의 크기와 같이 연동, 100Byte 이내 작성 가능
	@Size(max=100)
	private String subject;
	
	// null, 공백 => X
	@NotEmpty(message="본문 내용을 반드시 입력해야 하는 필수 항목입니다.")
	private String content;
	
	
}
